# react-native-comscore
## Description
`react-native-comscore` is a React Native package designed to seamlessly integrate ComScore analytics into your React Native applications. ComScore provides valuable insights into user behavior and engagement, allowing you to make informed decisions to improve your app's performance and user experience.

## Installation
To install `react-native-comscore`, you can use npm or yarn. Run one of the following commands in your project directory:

```
npm install react-native-comscore
``` 
or
```
yarn add react-native-comscore
```
## Usage
### Initialization
To initialize ComScore in your application, you need to provide your ComScore publisher ID. Additionally, you can include optional parameters such as the application name, usage properties auto-update mode, and auto-update interval.
```
import ComScore from 'react-native-comscore';

ComScore.initializeComScore({
  publisherId: 'YOUR_PUBLISHER_ID',
  applicationName: 'YourApplicationName',
  usagePropertiesAutoUpdateMode: 'FOREGROUND_ONLY',
  usagePropertiesAutoUpdateInterval: 60,
  // Add more optional parameters as needed...
});
``` 

## Tracking Navigation
Track user navigation and page views within your application by calling the ***trackNavigation*** method and providing the page name.
```
ComScore.trackNavigation('PageName');
```

## Updating Consent
Easily update the user consent status for ComScore tracking using the ***updateConsent*** method. Provide your publisher ID and the consent value.
```
ComScore.updateConsent('YOUR_PUBLISHER_ID', 'consentValue');
```

## Updating 1P Data
Use the ***updateData1P*** function to update additional first-party data (1P data) associated with your ComScore tracking.
#### Parameters
- **publisherId**: (required) Your ComScore publisher ID.
- **cs_fpid**: The first-party ID associated with the user.
- **cs_fpdm**: The first-party data metadata.
- **cs_fpit**: The first-party ID type.
- **cs_fpdt**: The first-party data type.
```
import ComScore from 'react-native-comscore';

// Example usage:
ComScore.updateData1P({
  publisherId: 'YOUR_PUBLISHER_ID',
  cs_fpid: 'USER_ID',
  cs_fpdm: 'METADATA',
  cs_fpit: 'ID_TYPE',
  cs_fpdt: 'DATA_TYPE'
});
```
This function allows you to dynamically update relevant first-party data points associated with your ComScore analytics, providing insights into user behavior and engagement. Ensure to include your ComScore publisher ID and any relevant data points you wish to update.

## License
## Contributing
## Acknowledgments
This package is based on [ComScore](https://www.comscore.com/), a leading provider of digital audience measurement and analytics.
