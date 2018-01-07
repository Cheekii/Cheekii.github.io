import React, { Component } from 'react';
import { Form, Text, Select, NestedForm } from 'react-form';
import {CountryDropdown, RegionDropdown} from './rcrs/rcrs';
import './Address.css';

class Address extends Component {
  constructor( props ) {
    super( props );

    this.state = {
      // fullname: "",
      // address_1: "",
      // address_2: "",
      // city: "",
      // country: "",
      // region: "",
      // zip: ""
    };
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(event) {
    const target = event.target;
    const value = target.type === 'checkbox' ? target.checked : target.value;
    const name = target.name;
    this.setState( {[name]: value});
    console.log(this.state);
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
                  field="fullname"
                  id="full-name"
                  placeholder="full name"
                  className="form-control"
                  required/>
                {/* <input
                  type="text"
                  name="fullname"
                  value={this.state.fullname}
                  onChange={this.handleChange}
                  id="full-name"
                  placeholder="full name"
                  className="form-control"
                  required /> */}
              </div>

              <div className="form-group">
                <label htmlFor="address-line-1">Address Line 1:</label>
                <Text
                  field="address_1"
                  id="address-line-1"
                  placeholder="address line 1"
                  className="form-control"
                  required/>
                {/* <input
                  type="text"
                  name="address_1"
                  value={this.state.address_1}
                  onChange={this.handleChange}
                  id="address-line-1"
                  placeholder="address line 1"
                  className="form-control"
                  required /> */}
                <small className="form-text text-muted">
                  Street address, P.O. box, company name, c/o
                </small>
              </div>

              <div className="form-group">
                <label htmlFor="address-line-2" >Address Line 2:</label>
                <Text
                  field="address_2"
                  id="address-line-2"
                  placeholder="address line 2"
                  className="form-control" />
                {/* <input
                  type="text"
                  name="address_2"
                  value={this.state.address_2}
                  onChange={this.handleChange}
                  id="address-line-2"
                  placeholder="address line 2"
                  className="form-control" /> */}
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
                {/* <input
                  type="text"
                  name="city"
                  value={this.state.city}
                  onChange={this.handleChange}
                  id="city"
                  placeholder="city"
                  className="form-control"
                  required /> */}
              </div>

              <div className="form-row">
                <div className="form-group col-md-5">
                  <label htmlFor="country">Country</label>
                  {/* <Select
                    field="country"
                    id="country"
                    className="form-control gds-cr"
                    country-data-region-id="region"
                    options={[]}
                    required /> */}
                  <CountryDropdown
                    field="country"
                    id="country"
                    whitelist={["CA"]}
                    className="form-control"
                    required/>
                  {/* <select
                    type="text"
                    name="country"
                    value={this.state.country}
                    onChange={this.handleChange}
                    id="country"
                    className="form-control gds-cr"
                    country-data-region-id="region"
                    required /> */}
                </div>
                <div className="form-group col-md-4">
                  <label htmlFor="region">State / Province / Region</label>
                  {/* <Select
                    field="state"
                    id="region"
                    className="form-control"
                    country-data-region-id="region"
                    options={[]}
                    required /> */}
                  <RegionDropdown
                    field="region"
                    id="region"
                    country={formApi.values.country}
                    value={formApi.values.region}
                    className="form-control"
                    required/>
                  {/* <select
                    type="text"
                    name="state"
                    value={this.state.state}
                    onChange={this.handleChange}
                    id="region"
                    className="form-control"
                    required /> */}
                </div>
                <div className="form-group col-md-3">
                  <label htmlFor="zip">Zip / Postal Code</label>
                  {/* <input
                    type="text"
                    name="zip"
                    value={this.state.zip}
                    onChange={this.handleChange}
                    id="zip"
                    className="form-control"
                    required /> */}
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
