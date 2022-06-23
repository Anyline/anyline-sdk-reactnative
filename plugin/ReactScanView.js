import {
    findNodeHandle,
    UIManager,
    requireNativeComponent
} from 'react-native';
import React, { useRef } from 'react';

const ScanView = requireNativeComponent('ScanView');

class ReactScanView extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            config: "",
            onResult: (event: Event) => {},
            onContinuousResult: (event: Event) => {}
        };
    }

    start() {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.ScanView.Commands.start,
            [],
        );
    }

    stop() {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.ScanView.Commands.stop,
            [],
        );
    }

    render() {
        return <ScanView
                    props={{ config: this.config }}
                    onResult={ this.onResult }
                    onContinuousResult={ this.onContinuousResult }
                    { ...this.props } />;
    }
}

export default ReactScanView;