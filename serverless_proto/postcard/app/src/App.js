import React, { Component } from 'react';
import Address from './Address';
import {Form} from 'react-form';

// import { Form, Text } from 'react-form';
import './App.css';

class App extends Component {
  constructor( props ) {
    super( props );
    this.state = {};
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleSubmit(event) {
    const target = event.target;
    console.log(target);
    // this.setState( { submittedValues });
  }

  render() {
    return (
      <div className="App">
        <div className="container">
          <Form onSubmit={this.handleSubmit}>
            { formApi => (
              <form onSubmit={formApi.submitForm} className="was-validated">
                <Address />
                <button type="submit" className="btn btn-primary">Submit</button>
              </form>
            )}
          </Form>

          {/* <form onSubmit={this.handleSubmit} className="was-validated">
                <Address />
                <button type="submit" className="btn btn-primary">Submit</button>
          </form> */}
        </div>
      </div>
    );
  }
}

export default App;
