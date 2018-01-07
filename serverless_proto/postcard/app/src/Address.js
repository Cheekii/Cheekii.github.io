import React, { Component } from 'react';
import { Form, Text, NestedForm } from 'react-form';
import {CountryDropdown, RegionDropdown} from './rcrs/rcrs';
import './Address.css';

class Address extends Component {
  constructor( props ) {
    super( props );

    this.state = {};
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(event) {
    const target = event.target;
    const value = target.type === 'checkbox' ? target.checked : target.value;
    const name = target.name;
    this.setState( {[name]: value});
  }
  render() {
    return (
      <NestedForm field={this.props.field}>
        <Form>
          { formApi => (
            <div className="Address">
              <div className="form-group">
                <label htmlFor="full-name">Full Name:</label>
                <Text
                  field="name"
                  id="full-name"
                  placeholder="full name"
                  className="form-control"
                  required/>
              </div>

              <div className="form-group">
                <label htmlFor="address-line-1">Address Line 1:</label>
                <Text
                  field="line1"
                  id="address-line-1"
                  placeholder="address line 1"
                  className="form-control"
                  required/>
                <small className="form-text text-muted">
                  Street address, P.O. box, company name, c/o
                </small>
              </div>

              <div className="form-group">
                <label htmlFor="address-line-2" >Address Line 2:</label>
                <Text
                  field="line2"
                  id="address-line-2"
                  placeholder="address line 2"
                  className="form-control" />
                <small className="form-text text-muted">
                  Apartment, suite , unit, building, floor, etc.
                </small>
              </div>

              <div className="form-group">
                <label htmlFor="city">City</label>
                <Text
                  field="city"
                  id="city"
                  placeholder="city"
                  className="form-control"
                  required/>
              </div>

              <div className="form-row">
                <div className="form-group col-md-5">
                  <label htmlFor="country">Country</label>
                  <CountryDropdown
                    field="country"
                    id="country"
                    whitelist={["CA"]}
                    className="form-control"
                    required/>
                </div>
                <div className="form-group col-md-4">
                  <label htmlFor="region">State / Province / Region</label>
                  <RegionDropdown
                    field="state"
                    id="region"
                    country={formApi.values.country}
                    value={formApi.values.state}
                    className="form-control"
                    required/>
                </div>
                <div className="form-group col-md-3">
                  <label htmlFor="zip">Zip / Postal Code</label>
                  <Text
                    field="zip"
                    id="zip"
                    className="form-control"
                    required/>
                </div>
              </div>
            </div>
        )}
      </Form>
    </NestedForm>
    );
  }
}

export default Address;
