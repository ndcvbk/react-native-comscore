/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React, { useEffect } from "react";
import { type Data1p, UsagePropertiesAutoUpdateMode } from "./utility/type";
import AppNavigator from "./config/navigation";
import { initializeComscore } from "react-native-comscore";


const PUBLISHER_ID = "16676137";

async function initialize() {
  let data_1p: Data1p = {
    cs_fpid: "1605266069802_50777152",
    cs_fpit: "c",
    cs_fpdm: "39642313001",
    cs_fpdt: "01",
  };
  try {
    await initializeComscore({
      publisherId: PUBLISHER_ID,
      applicationName: "News Magazine",
      usagePropertiesAutoUpdateMode:
        UsagePropertiesAutoUpdateMode.FOREGROUND_AND_BACKGROUND,
      usagePropertiesAutoUpdateInterval: 120,
      data_1p: data_1p,
    });
  } catch (e) {
    console.log("Error in intializing comscore", e);
  }

}

const App = () => {
  useEffect(() => {
    initialize();
  }, []);

  return <AppNavigator />;
};

export default App;