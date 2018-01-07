import React, { Component } from 'react';
import Address from './Address';
import {Form, TextArea} from 'react-form';
import {CountryDropdown} from './rcrs/rcrs';
import logo from './logo.svg';
import './App.css';

class App extends Component {
  constructor( props ) {
    super( props );
    this.state = {};
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleSubmit(event) {
    const target = event.target;
    // this.setState( { submittedValues });
  }

  render() {
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
        </header>
        <div className="container">
          <Form onSubmit={this.handleSubmit}>
            { formApi => (
              <form onSubmit={formApi.submitForm} className="was-validated">
                <div id="accordion" role="tablist">

                  <div className="card">
                    <div class="card-header" role="tab" id="headingOne">
                      <h5 class="mb-0">
                        <a data-toggle="collapse" href="#collapseOne" role="button" aria-expanded="true" aria-controls="collapseOne">
                          From Mailing Address
                        </a>
                      </h5>
                    </div>

                    <div id="collapseOne" class="collapse show" role="tabpanel" aria-labelledby="headingOne" data-parent="#accordion">
                      <div class="card-body">
                        <Address field="fromAddress"/>
                      </div>
                    </div>
                  </div>

                  <div className="card">
                    <div class="card-header" role="tab" id="headingTwo">
                      <h5 class="mb-0">
                        <a data-toggle="collapse" href="#collapseTwo" role="button" aria-expanded="true" aria-controls="collapseTwo">
                          To Mailing Address
                        </a>
                      </h5>
                    </div>

                    <div id="collapseTwo" class="collapse show" role="tabpanel" aria-labelledby="headingTwo" data-parent="#accordion">
                      <div class="card-body">
                        <Address field="toAddress"/>
                      </div>
                    </div>
                  </div>

                  <div className="card">
                    <div class="card-header" role="tab" id="headingThree">
                      <h5 class="mb-0">
                        <a data-toggle="collapse" href="#collapseThree" role="button" aria-expanded="true" aria-controls="collapseThree">
                          Message
                        </a>
                      </h5>
                    </div>

                    <div id="collapseThree" class="collapse show" role="tabpanel" aria-labelledby="headingThree" data-parent="#accordion">
                      <div class="card-body">
                        <TextArea
                          field="message"
                          name="message"
                          className="form-control"
                          rows="10"
                          required/>
                      </div>
                    </div>
                  </div>

                </div>

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
