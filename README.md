# README #

## Requirements

#### iOS
Currently not supported, will be added in later releases

#### Android
minSDK >= 21


## How to use the SDK

#### 0. Set up your development environment
To set up a React Native application please follow the instructions from [reactnative.dev](https://reactnative.dev/docs/environment-setup).

#### 1. Install the Anyline plugin
Add the dependency to your project and link the plugin via react-native.
```shell
yarn add @anyline/anyline-sdk-react-native && react-native link
```

Add our maven repository to the project level build.gradle.
```groovy
allprojects {
    repositories {
        maven { url 'https://anylinesdk.blob.core.windows.net/maven/' }
    }
}
```

#### 2. Add required permissions to the Manifest.xml
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
  package="com.anyline.example.reactnative">
    ...
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.CAMERA" />
    ...
</manifest>
```

#### 3. Import AnylineSdk and ReactScanView
```js
import { AnylineSdk, ReactScanView } from '@anyline/anyline-sdk-react-native';
```

#### 4. Initialise SDK
Initialise the Anyline SDK either with your [Trial License](https://ocr.anyline.com/request/sdk-trial) or with a [Commercial License](https://ocr.anyline.com/contact).  
You can optionally listen to success and error callbacks for the initialisation.

```js
class App extends React.Component {
    constructor() {
        super();
        
        AnylineSdk.initSdk(
            "<YOUR LICENSE KEY>",
            (error) => {
                // handle initialisation error
            },
            () => {
                // handle intialisation success
            }
        );
    }
}
```

#### 5. Add ReactScanView to your view hierarchy
You should create a reference ot the `ReactScanView` to be able to listen to start and stop the scan easily.

Pass the JSON config file and the result callback method as an attribute to the `ReactScanView`.
The config file should be in the assets folder of the Android application located in `/android/app/src/main/assets`.

```js
class App extends React.Component {
    constructor() {
        ...
        this.reactScanView = React.createRef();
        this._onResult = this._onResult.bind(this);
        ...
    }

    render() {
        <ReactScanView
            ref = { this.reactScanView }
            style = {{ width: '100%',  height: '100%' }}
            config = "barcode_view_config.json"
            onResult = { this._onResult }
        />
    }
}
```

#### 6. Start Scanning
You can start the scan process whenever you wish, but you should not forget to stop the process once the component is dismounting.
You will receive the result in the `_onResult` callback which you pass to the `ReactScanView`.

```js
componentDidMount() {
    this.reactScanView.current.start();
}

componentWillUnmount() {
    this.reactScanView.current.stop();
}

_onResult(event: Event) {
    const barcodes = JSON.parse(event.nativeEvent.result);
    console.log(barcodes);
}
```

## Known Limitations 

Currently this ReactNative plugin only supports Barcode scanning and Android as a platform.
More products as well as iOS support will be added in the following releases.


## Get help

Please raise a support request using the [Anyline Helpdesk](https://anyline.atlassian.net/servicedesk/customer/portal/2/group/6). When raising a support request, please fill out and include the following information:

`Support request concerning Anyline Github Repository: anyline-sdk-react-native`


## License

See LICENSE file.