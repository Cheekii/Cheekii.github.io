import React, { Component } from 'react';
import Address from './Address';
// import { Form, Text } from 'react-form';
import './App.css';

class App extends Component {
  constructor( props ) {
    super( props );
    this.state = {};
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleSubmit(submittedValues) {
    this.setState( { submittedValues });
  }

  render() {
    return (
      <div className="App">
        <div className="container">
          {/* <Form onSubmit={this.handleSubmit} className="form-horizontal">
            { formApi => (
              <form onSubmit={formApi.submitForm}>
                <div className="form-group">
                  <label htmlFor="firstName" >Full Name</label>
                  <Text field="firstName" id="firstName" className="form-control" placeholder="full name"/>
                </div>
                <div className="control-group">
                    <label className="control-label">Full Name</label>
                    <div className="controls">
                        <input id="full-name" name="full-name" type="text" placeholder="full name" className="input-xlarge" />
                        <p className="help-block"></p>
                    </div>
                </div>
                <button type="submit">Submit</button>
              </form>
            )}
          </Form> */}

          <form onSubmit={this.handleSubmit} className="was-validated">
                {/* full-name input */}
                <Address />
                <button type="submit" className="btn btn-primary">Submit</button>
              </form>
        </div>
      </div>
    );
  }
}

export default App;
