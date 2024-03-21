/**
 * @format
 */

import {AppRegistry} from 'react-native';
import App from './App';
import {name as appName} from './app.json';

AppRegistry.registerComponent(appName, () => App);
import {NativeModules, Platform} from 'react-native';

const {ActivityRecognition} = NativeModules;

// Request permission function
const requestActivityRecognitionPermission = async () => {
  if (Platform.OS === 'android') {
    try {
      const hasPermission = await ActivityRecognition.requestPermission();
      console.log('Permission status:', hasPermission);
    } catch (e) {
      console.error(e);
    }
  }
};

requestActivityRecognitionPermission()

