module.exports = {
  dependencies: {
    'react-native-comscore': {
      platforms: {
        android: {
          sourceDir: '../node_modules/react-native-comscore/android/',
          packageImportPath: 'import com.comscorereactnative.ComScorePackage;',
        },
        ios: {
          podspecPath: '../node_modules/react-native-comscore/react-native-comscore.podspec',
        },
      },
    },
  },
};