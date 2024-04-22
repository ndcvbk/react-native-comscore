/**
 * Enum representing the auto-update modes for usage properties.
 */
type UsagePropertiesAutoUpdateMode =
  | 'FOREGROUND_ONLY'
  | 'FOREGROUND_AND_BACKGROUND'
  | 'DISABLED';

/**
 * Interface representing the data_1p object used in ComScoreParams.
 */
type Data1p = {
  publisherId?: string; // The publisher ID.
  cs_fpid?: string; // First-party ID.
  cs_fpit?: string; // First-party ID timestamp.
  cs_fpdm?: string; // First-party ID domain.
  cs_fpdt?: string; // First-party ID TTL (Time To Live).
};

/**
 * Interface representing the parameters for initializing ComScore.
 */
type ComScoreParams = {
  publisherId: string; // The publisher ID.
  applicationName?: string; // The name of the application.
  usagePropertiesAutoUpdateMode?: UsagePropertiesAutoUpdateMode; // The auto-update mode for usage properties.
  usagePropertiesAutoUpdateInterval?: number; // The interval for auto-updating usage properties.
  data_1p?: Data1p; // First-party data parameters.
};

declare class Comscore {
  /**
   * Initializes ComScore with the provided parameters.
   * @param params The ComScore initialization parameters.
   *               - publisherId: The publisher ID.
   *               - applicationName?: The name of the application (optional).
   *               - usagePropertiesAutoUpdateMode?: The auto-update mode for usage properties (optional).
   *               - usagePropertiesAutoUpdateInterval?: The interval for auto-updating usage properties (optional).
   *               - data_1p?: First-party data parameters (optional).
   */
  static initializeComscore(params: ComScoreParams): void;

  /**
   * Tracks a screen with the given screen name.
   * @param screenName The name of the screen to track.
   */
  static trackScreen(screenName: string): void;

  /**
   * Updates the consent state.
   * @param consentState The new consent state.
   */
  static updateConsent(consentState: string): void;

  /**
   * Updates the first-party data parameters.
   * @param params The updated first-party data parameters.
   *               - publisherId: The publisher ID (required).
   *               - cs_fpid?: First-party ID (optional).
   *               - cs_fpit?: First-party ID timestamp (optional).
   *               - cs_fpdm?: First-party ID domain (optional).
   *               - cs_fpdt?: First-party ID TTL (Time To Live) (optional).
   */
  static updateData1P(params: Data1p): void;
}

export = Comscore;
