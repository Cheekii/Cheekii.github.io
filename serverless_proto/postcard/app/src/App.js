import React, { Component } from 'react';
import Address from './Address';
import ImageFile from './ImageFile';
import {Form, TextArea} from 'react-form';
import StripeCheckout from 'react-stripe-checkout';
import logo from './logo.svg';
import './App.css';

function StripeOrSubmiton(props) {
  const token = props.token;
  const onToken = props.onToken;
  if (token) {
    return <button type="submit" className="btn btn-primary">Submit</button>
  }
  else {
    return <StripeCheckout
      token={onToken}
      stripeKey="pk_test_SfLgmhoTnS4gYa22nDw6q5lX"
      amount={1000}
      currency="CAD"
      >
      <button className="btn btn-primary">
        Pay
      </button>
    </StripeCheckout>
  }
}

class App extends Component {
  constructor( props ) {
    super( props );
    this.state = {};
    this.handleSubmit = this.handleSubmit.bind(this);
    this.onToken = this.onToken.bind(this);
  }


  onToken(token){
    this.setState({token: token});
  }

  handleSubmit(event) {
    // const target = event.target;
    const stripe = "tok_visa";
    fetch('https://e932hmebne.execute-api.us-west-2.amazonaws.com/dev/processOrder', {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        stripe: this.token,
        name: event.toAddress.name,
        message: event.message,
        toAddress: event.toAddress,
        fromAddress: event.fromAddress,
        base64image: event.image
      })
    })
    console.log(stripe);
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
                    <div className="card-header" role="tab" id="headingOne">
                      <h5 className="mb-0">
                        <a data-toggle="collapse" href="#collapseOne" role="button" aria-expanded="true" aria-controls="collapseOne">
                          From Mailing Address
                        </a>
                      </h5>
                    </div>

                    <div id="collapseOne" className="collapse show" role="tabpanel" aria-labelledby="headingOne" data-parent="#accordion">
                      <div className="card-body">
                        <Address field="fromAddress"/>
                      </div>
                    </div>
                  </div>

                  <div className="card">
                    <div className="card-header" role="tab" id="headingTwo">
                      <h5 className="mb-0">
                        <a data-toggle="collapse" href="#collapseTwo" role="button" aria-expanded="true" aria-controls="collapseTwo">
                          To Mailing Address
                        </a>
                      </h5>
                    </div>

                    <div id="collapseTwo" className="collapse show" role="tabpanel" aria-labelledby="headingTwo" data-parent="#accordion">
                      <div className="card-body">
                        <Address field="toAddress"/>
                      </div>
                    </div>
                  </div>

                  <div className="card">
                    <div className="card-header" role="tab" id="headingThree">
                      <h5 className="mb-0">
                        <a data-toggle="collapse" href="#collapseThree" role="button" aria-expanded="true" aria-controls="collapseThree">
                          Message
                        </a>
                      </h5>
                    </div>

                    <div id="collapseThree" className="collapse show" role="tabpanel" aria-labelledby="headingThree" data-parent="#accordion">
                      <div className="card-body">
                        <TextArea
                          field="message"
                          name="message"
                          className="form-control"
                          rows="10"
                          required/>
                      </div>
                    </div>
                  </div>

                  <div className="card">
                    <div className="card-header" role="tab" id="headingFour">
                      <h5 className="mb-0">
                        <a data-toggle="collapse" href="#collapseFour" role="button" aria-expanded="true" aria-controls="collapseFour">
                          Image
                        </a>
                      </h5>
                    </div>

                    <div id="collapseFour" className="collapse show" role="tabpanel" aria-labelledby="headingFour" data-parent="#accordion">
                      <div className="card-body">
                        <ImageFile
                          field="image"
                          name="image"
                          className="form-control"
                          required/>
                      </div>
                    </div>
                  </div>

                </div>

                <StripeOrSubmiton token={this.state.token} onToken={this.onToken}/>
              </form>
            )}
          </Form>
        </div>
      </div>
    );
  }
}

export default App;
