#import "ComScoreModule.h"
#import <ComScore/ComScore.h>
#import <React/RCTLog.h>
#import <React/RCTConvert.h>
#import <React/RCTBridge.h>

@implementation ComScoreModule
@synthesize bridge = _bridge;

NSString *_publisherId;

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(initializeComScore:(NSDictionary *)params) {
    // Extract required parameter
    _publisherId = params[@"publisherId"];
    if (!_publisherId) {
        RCTLogError(@"ComScoreModule - Error: publisherId is missing.");
        return;
    }
    RCTLog(@"ComScoreModule - Setting publisherId: %@", _publisherId);

    // Extract optional parameters
    NSString *applicationName = params[@"applicationName"];
    NSString *usagePropertiesAutoUpdateMode = params[@"usagePropertiesAutoUpdateMode"];
    NSNumber *usagePropertiesAutoUpdateInterval = params[@"usagePropertiesAutoUpdateInterval"];

    // Initialize ComScore
    RCTLog(@"ComScoreModule - Initializing ComScore");

    // 1P-Data
    NSMutableDictionary *labels = [NSMutableDictionary dictionary];
    NSDictionary *data1p = params[@"data_1p"];
    if (data1p) {
        RCTLog(@"ComScoreModule - Data_1p is not nil");
        labels[@"cs_fpid"] = data1p[@"cs_fpid"];
        labels[@"cs_fpit"] = data1p[@"cs_fpit"];
        labels[@"cs_fpdm"] = data1p[@"cs_fpdm"];
        labels[@"cs_fpdt"] = data1p[@"cs_fpdt"];
    }

    @try {
        // Create a PublisherConfiguration with the provided settings
        SCORPublisherConfiguration *publisherConfig;
        publisherConfig = [SCORPublisherConfiguration publisherConfigurationWithBuilderBlock:^(SCORPublisherConfigurationBuilder *builder) {
            builder.publisherId = _publisherId;
            if (labels.count > 0) {
                builder.persistentLabels = labels;
            }
        }];

        // Apply applicationName if provided
        if (applicationName && applicationName.length > 0) {
            RCTLog(@"ComScoreModule - Setting applicationName: %@", applicationName);
            [SCORAnalytics configuration].applicationName = applicationName;
        }

        // Apply general configuration settings if provided
        if (usagePropertiesAutoUpdateMode && usagePropertiesAutoUpdateMode.length > 0) {
            // Parse the update mode string into the corresponding enum value
            SCORUsagePropertiesAutoUpdateMode autoUpdateMode = [self parseUpdateMode:usagePropertiesAutoUpdateMode];
            RCTLog(@"ComScoreModule - Setting usagePropertiesAutoUpdateMode: %ld", (long)autoUpdateMode);
            [SCORAnalytics configuration].usagePropertiesAutoUpdateMode = autoUpdateMode;
        }

        // Apply usagePropertiesAutoUpdateInterval if provided
        if (usagePropertiesAutoUpdateInterval && [usagePropertiesAutoUpdateInterval integerValue] > 0) {
            RCTLog(@"ComScoreModule - Setting usagePropertiesAutoUpdateInterval: %@", usagePropertiesAutoUpdateInterval);
            [SCORAnalytics configuration].usagePropertiesAutoUpdateInterval = [usagePropertiesAutoUpdateInterval integerValue];
        }

        [[SCORAnalytics configuration] addClientWithConfiguration:publisherConfig];
        [SCORAnalytics.configuration enableImplementationValidationMode];

        // Start the ComScore library
        [SCORAnalytics start];
    }
    @catch (NSException *exception) {
        RCTLogError(@"ComScoreModule - Error initializing ComScore: %@", exception.reason);
    }
}

RCT_EXPORT_METHOD(updateConsent:(NSString *)consentValue) {
    if (!_publisherId) {
        RCTLogError(@"ComScoreModule - Error: publisherId is missing.");
        return;
    }
    RCTLog(@"ComScoreModule - updateConsent: %@", consentValue);
    @try {
        [((SCORClientConfiguration *) [[SCORAnalytics configuration] publisherConfigurationWithPublisherId:_publisherId]) setPersistentLabelWithName:@"cs_ucfr" value:consentValue];
        [SCORAnalytics notifyHiddenEvent];
    }
    @catch (NSException *exception) {
        RCTLogError(@"ComScoreModule - Error in updateConsent: %@", exception.reason);
    }
}

RCT_EXPORT_METHOD(trackScreen:(NSString *)pageName) {
    if (!pageName) {
        RCTLogError(@"ComScoreModule - trackScreen: pageName is nil.");
        return;
    }
    RCTLog(@"ComScoreModule - trackScreen page name: %@", pageName);
    @try {
        [SCORAnalytics notifyViewEventWithLabels:@{@"ns_category": pageName}];
        RCTLog(@"ComScoreModule - notifyViewEvent called successfully.");
    }
    @catch (NSException *exception) {
        RCTLogError(@"ComScoreModule - Error in trackScreen: %@", exception.reason);
    }
}

RCT_EXPORT_METHOD(trackScreenWithData:(NSString *)pageName data:(NSDictionary *)additionalParams) {
    if (!pageName) {
        RCTLogError(@"ComScoreModule - trackScreen: pageName is nil.");
        return;
    }
    RCTLog(@"ComScoreModule - trackScreen page name: %@", pageName);
    @try {
        // Create a mutable dictionary to hold the parameters
        NSMutableDictionary<NSString *, NSString *> *params = [NSMutableDictionary dictionaryWithDictionary:@{@"ns_category": pageName}];

        // Merge additionalParams into params
        if (additionalParams) {
            [params addEntriesFromDictionary:additionalParams];
        }
        
        [SCORAnalytics notifyViewEventWithLabels:params];
        RCTLog(@"ComScoreModule - notifyViewEvent called successfully.");
    }
    @catch (NSException *exception) {
        RCTLogError(@"ComScoreModule - Error in trackScreen: %@", exception.reason);
    }
}

RCT_EXPORT_METHOD(update1PData:(NSDictionary *)params) {
    NSString *publisherId = params[@"publisherId"];
    if (!publisherId) {
        RCTLogError(@"ComScoreModule - Error: publisherId is missing.");
        return;
    }

    @try {
        for (NSString *key in params.allKeys) {
            if (![key isEqualToString:@"publisherId"]) {
                [((SCORClientConfiguration *) [[SCORAnalytics configuration] publisherConfigurationWithPublisherId:publisherId]) setPersistentLabelWithName:key value:params[key]];
                RCTLog(@"ComScoreModule - Setting persistent label: %@ - %@", key, params[key]);
            }
        }
        [SCORAnalytics notifyHiddenEvent];
        RCTLog(@"ComScoreModule - 1p data updated.");
    }
    @catch (NSException *exception) {
        RCTLogError(@"ComScoreModule - Error updating 1P data: %@", exception.reason);
    }
}

// Helper method to parse string representation into UsagePropertiesAutoUpdateMode enum
- (SCORUsagePropertiesAutoUpdateMode)parseUpdateMode:(NSString *)modeString {
    if ([modeString isEqualToString:@"FOREGROUND_ONLY"]) {
        return SCORUsagePropertiesAutoUpdateModeForegroundOnly;
    } else if ([modeString isEqualToString:@"FOREGROUND_AND_BACKGROUND"]) {
        return SCORUsagePropertiesAutoUpdateModeForegroundAndBackground;
    } else if ([modeString isEqualToString:@"DISABLED"]) {
        return SCORUsagePropertiesAutoUpdateModeDisabled;
    } else {
        return SCORUsagePropertiesAutoUpdateModeForegroundOnly; // Default value
    }
}

@end
