/* @flow */
import React, { Component } from "react";

import Box from "grommet/components/Box";
import Spinning from "grommet/components/icons/Spinning";

class Loading extends Component {
  props: {
    busy: boolean
  };
  render() {
    if (this.props.busy) {
      return (
        <Box pad={{ vertical: "large" }}>
          <Box align="center" alignSelf="center" pad={{ vertical: "large" }}>
            Loading ...
            <Spinning />
          </Box>
        </Box>
      );
    } else {
      return null;
    }
  }
}

export default Loading;
