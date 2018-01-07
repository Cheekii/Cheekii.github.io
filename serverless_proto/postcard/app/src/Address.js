import React, { Component } from 'react';
import { CountryDropdown, RegionDropdown } from 'react-country-region-selector';
import './Address.css';

class Address extends Component {
  constructor(props) {
    super(props);
    this.countries = props.countries;
    this.state = { country: '', region: '' };
    this.selectCountry = this.selectCountry.bind(this);
    this.selectRegion = this.selectRegion.bind(this);
  }

  selectCountry(val) {
    this.setState({ country: val });
  }

  selectRegion(val) {
    this.setState({ region: val });
  }
  render() {
    const { country, region } = this.state;
    return (
      <div className="Address">
        <div className="form-group">
          <label htmlFor="full-name">Full Name:</label>
          <input id="full-name" name="full-name" type="text" placeholder="full name" className="form-control" required />
        </div>

        <div className="form-group">
          <label htmlFor="address-line-1">Address Line 1:</label>
          {/* <div className="col-sm-9"> */}
          <input id="address-line-1" name="address-line-1" type="text" placeholder="address line 1" className="form-control" required />
          <small className="form-text text-muted">
            Street address, P.O. box, company name, c/o
                    </small>
          {/* </div> */}
        </div>

        <div className="form-group">
          <label htmlFor="address-line-2" >Address Line 2:</label>
          <input id="address-line-2" name="address-line-2" type="text" placeholder="address line 2" className="form-control" />
          <small className="form-text text-muted">
            Apartment, suite , unit, building, floor, etc.
          </small>
        </div>

        <div className="form-group">
          <label htmlFor="city">City</label>
          <input id="city" name="city" type="text" placeholder="city" className="form-control" required />
        </div>

        <div className="form-group">
          <label htmlFor="country2">Country2</label>
          <select id="country2" name="country2" type="text" className="form-control gds-cr gds-countryflag" country-data-region-id="gds-cr-1" required />
        </div>

        <div className="form-group">
          <label htmlFor="region">Region</label>
          <select id="region" name="region" type="text" className="form-control" id="gds-cr-1" required />
        </div>

        <div className="form-row">
          <div className="form-group col-md-6">
            <label htmlFor="country">Country</label>
            {/* <input type="text" className="form-control" id="inputCity" /> */}
            <CountryDropdown
              id="country"
              classes="form-control"
              value={country}
              onChange={this.selectCountry}
              whitelist={this.countries}/>
          </div>
          <div className="form-group col-md-4">
            <label htmlFor="state">State / Province / Region</label>
            <RegionDropdown
              id="state"
              classes="form-control"
              country={country}
              value={region}
              onChange={(val) => this.selectRegion(val)} />
          </div>
          <div className="form-group col-md-2">
            <label htmlFor="inputZip">Zip</label>
            <input type="text" className="form-control" id="inputZip" required/>
          </div>
        </div>
      </div>
    );
  }
}

export default Address;
