import React, { Component } from 'react';
import { FormField } from 'react-form';
import './Address.css';

class AddressWrapper extends Component {
  constructor( props ) {
    super( props );

    const {
      fieldApi,
      onInput
    } = this.props;

    const {
      getValue,
      getError,
      getWarning,
      getSuccess,
      setValue,
      setTouched,
    } = fieldApi;

    const error = getError();
    const warning = getWarning();
    const success = getSuccess();

    this.state = {
      fullname: "",
      address_1: "",
      address_2: "",
      city: "",
      country: "",
      region: "",
      zip: ""
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
      <div className="Address">
        <div className="form-group">
          <label htmlFor="full-name">Full Name:</label>
          <input
            type="text"
            name="fullname"
            value={this.state.fullname}
            onChange={this.handleChange}
            id="full-name"
            placeholder="full name"
            className="form-control"
            required />
        </div>

        <div className="form-group">
          <label htmlFor="address-line-1">Address Line 1:</label>
          <input
            type="text"
            name="address_1"
            value={this.state.address_1}
            onChange={this.handleChange}
            id="address-line-1"
            placeholder="address line 1"
            className="form-control"
            required />
          <small className="form-text text-muted">
            Street address, P.O. box, company name, c/o
          </small>
        </div>

        <div className="form-group">
          <label htmlFor="address-line-2" >Address Line 2:</label>
          <input
            type="text"
            name="address_2"
            value={this.state.address_2}
            onChange={this.handleChange}
            id="address-line-2"
            placeholder="address line 2"
            className="form-control" />
          <small className="form-text text-muted">
            Apartment, suite , unit, building, floor, etc.
          </small>
        </div>

        <div className="form-group">
          <label htmlFor="city">City</label>
          <input
            type="text"
            name="city"
            value={this.state.city}
            onChange={this.handleChange}
            id="city"
            placeholder="city"
            className="form-control"
            required />
        </div>

        <div className="form-row">
          <div className="form-group col-md-5">
            <label htmlFor="country">Country</label>
            <select
              type="text"
              name="country"
              value={this.state.country}
              onChange={this.handleChange}
              id="country"
              className="form-control gds-cr"
              country-data-region-id="region"
              required />
          </div>
          <div className="form-group col-md-4">
            <label htmlFor="region">State / Province / Region</label>
            <select
              type="text"
              name="state"
              value={this.state.state}
              onChange={this.handleChange}
              id="region"
              className="form-control"
              required />
          </div>
          <div className="form-group col-md-3">
            <label htmlFor="zip">Zip / Postal Code</label>
            <input
              type="text"
              name="zip"
              value={this.state.zip}
              onChange={this.handleChange}
              id="zip"
              className="form-control"
              required />
          </div>
        </div>
      </div>
    );
  }
}

const Address = FormField(AddressWrapper);

export default Address;
