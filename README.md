# react-native-comscore

## Description
`react-native-comscore` is a React Native package designed to seamlessly integrate ComScore analytics into your React Native applications. ComScore provides valuable insights into user behavior and engagement, allowing you to make informed decisions to improve your app's performance and user experience.

## Installation
To install `react-native-comscore` from GitHub, you can follow these steps:

```
 yarn install https://github.com/dev-codesymphony/react-native-Comscore.git
``` 

OR 
```
 npm install https://github.com/dev-codesymphony/react-native-Comscore.git
```

## iOS optional requirement
It is advisable to add **AddSupport.framework** to your project target(s) even if the application does not contain advertisements because this enables the Comscore library to collect the **advertisingIdentifier** value for estimating the number of unique users for audience research purposes.

## Usage
### Initialization
To initialize ComScore in your application, you need to provide your ComScore publisher ID. Additionally, you can include optional parameters such as the application name, usage properties auto-update mode, auto-update interval and 1P data.
##### Parameters:
- ****publisherId****: (required) Your ComScore publisher ID.
- ****applicationName****: (optional) The name of your application.
- ****usagePropertiesAutoUpdateMode****: (optional) The mode for updating properties (_FOREGROUND_ONLY_, _FOREGROUND_AND_BACKGROUND_, _DISABLED_).
- ****usagePropertiesAutoUpdateInterval****: (optional) The interval for usage properties (in seconds).
- ****data_1p****: (optional) An object containing 1P data:
    - ****cs_fpid****: (optional) The first-party ID associated with the user.
    - ****cs_fpdm****: (optional) The first-party data metadata.
    - ****cs_fpit****: (optional) The first-party ID type.
    - ****cs_fpdt****: (optional) The first-party data type.
```
import Comscore from 'react-native-comscore';

const params = {
  publisherId: 'YOUR_PUBLISHER_ID',
  applicationName: 'YourApplicationName',
  usagePropertiesAutoUpdateMode: 'FOREGROUND_ONLY',
  usagePropertiesAutoUpdateInterval: 60,
  data_1p: {
    cs_fpid: 'your_cs_fpid_value',
    cs_fpdm: 'your_cs_fpdm_value',
    cs_fpit: 'your_cs_fpit_value',
    cs_fpdt: 'your_cs_fpdt_value',
  },
};

Comscore.initializeComScore(params);
``` 
Initialize ComScore with your publisher ID and other optional parameters to start tracking analytics for your React Native application.

### Tracking Navigation
Track user navigation and page views within your application by calling the ***trackScreen*** method and providing the page name.
```
import Comscore from 'react-native-comscore';

// Example usages:
Comscore.trackScreen('PageName');

// Track screen with additional data
Comscore.trackScreenWithData('PageName', {
  type: 'type',
  action: 'action'
});
```

### Updating Consent
Easily update the user consent status for ComScore tracking using the updateConsent method. Provide the consent value to be updated.
```
import Comscore from 'react-native-comscore';

// Example usage:
Comscore.updateConsent('consentValue');
```
This method allows you to dynamically update the consent status for ComScore tracking within your React Native application.

### Updating 1P Data
Use the ***updateData1P*** function to update additional first-party data (1P data) associated with your ComScore tracking.
##### Parameters
- ****publisherId****: (required) Your ComScore publisher ID.
- ****cs_fpid****: The first-party ID associated with the user.
- ****cs_fpdm****: The first-party data metadata.
- ****cs_fpit****: The first-party ID type.
- ****cs_fpdt****: The first-party data type.
```
import Comscore from 'react-native-comscore';

// Example usage:
Comscore.updateData1P({
  publisherId: 'YOUR_PUBLISHER_ID',
  cs_fpid: 'USER_ID',
  cs_fpdm: 'METADATA',
  cs_fpit: 'ID_TYPE',
  cs_fpdt: 'DATA_TYPE'
});
```
This function allows you to dynamically update relevant first-party data points associated with your ComScore analytics, providing insights into user behavior and engagement. Ensure to include your ComScore publisher ID and any relevant data points you wish to update.

## Function Parameters
| Function                  | Parameter                             | Type                           | Required      | Default Value | Description                                                                              |
|---------------------------|---------------------------------------|--------------------------------|---------------|---------------|------------------------------------------------------------------------------------------|
| `initializeComscore`      | `publisherId`                         | `string`                       | Yes           | -             | Your ComScore publisher ID.                                                              |
|                           | `applicationName`                    | `string \| undefined`          | No            | `undefined`   | The name of your application.                                                            |
|                           | `usagePropertiesAutoUpdateMode`      | `string \| undefined`          | No            | `undefined`   | The mode for updating usage properties (FOREGROUND_ONLY, FOREGROUND_AND_BACKGROUND, DISABLED).|
|                           | `usagePropertiesAutoUpdateInterval`  | `number \| undefined`          | No            | `undefined`   | The interval for updating usage properties (in seconds).                                  |
|                           | `data_1p`                            | `Data1p \| undefined`          | No            | `undefined`   | An object containing 1P data.                                                             |
| `trackScreen`             | `screenName`                         | `string`                       | Yes           | -             | Tracks the screen with the provided screen name.                                          |
| `trackScreenWithData`     | `screenName`                         | `string`                       | Yes           | -             | The name of the screen to track.                                                         |
|                           | `data`                                | `object`          | Yes            | -   | An object containing additional data to track along with the screen.                      |
| `updateConsent`           | `consentState`                       | `string`                       | Yes           | -             | Updates the consent state for ComScore tracking.                                          |
| `updateData1P`            | `publisherId`                        | `string`                       | Yes           | -             | Updates additional first-party data (1P data) associated with ComScore tracking.          |
|                           | `cs_fpid`                            | `string \| undefined`          | No            | `undefined`   | The first-party ID associated with the user.                                              |
|                           | `cs_fpdm`                            | `string \| undefined`          | No            | `undefined`   | The first-party data metadata.                                                            |
|                           | `cs_fpit`                            | `string \| undefined`          | No            | `undefined`   | The first-party ID type.                                                                  |
|                           | `cs_fpdt`                            | `string \| undefined`          | No            | `undefined`   | The first-party data type.                                                                |




## License
This project is open-source.
## Contributing

Contributors:
- [Hardik Mashru](https://github.com/harrymash2006)
- [Yusuf Sham](https://github.com/yusufsham)
- [Chandan Dass](https://github.com/chandandass)
  
We welcome contributions from the community to improve `react-native-comscore`. If you'd like to contribute, please feel free to submit a pull request with your changes or open an issue with any suggestions or bug reports. Your contributions are greatly appreciated!

## Acknowledgments
This package is based on [ComScore](https://www.Comscore.com/), a leading provider of digital audience measurement and analytics.

