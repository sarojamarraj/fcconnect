import React, { Component } from 'react';

class ClassDensityCalculator extends Component {
  constructor(props) {
    super(props);
    this.state = {
      density: '',
      freightClass: '',
    };
    this.inputs = {
      length: 0,
      width: 0,
      height: 0,
      weight: 0,
    };
  }

  render() {
    const styles = {
      container: {
        background: '#fff',
        padding: '10px',
      },
      title: {
        fontSize: '15px',
        color: '#666',
        margin: '0 0 10px 0',
        fontWeight: 'bold',
      },
      input: {
        margin: '0',
        padding: '0',
      },
      table: {
        width: '100%',
      },
      tableTd: {
        verticalAlign: 'bottom',
      },
    };

    return (
      <div className="well well-default" style={styles.container}>
        <h3 style={styles.title}>Class and Density Calculator</h3>

        <table className="table" style={styles.table}>
          <tbody>
            <tr>
              <td style={styles.tableTd}>
                <section style={styles.input}>
                  <label>Length</label>
                  <label className="input">
                    <input
                      type="text"
                      name="length"
                      className="form-control input-xs"
                      placeholder="inches"
                      onChange={this.handleInputChange}
                      size="6"
                    />
                  </label>
                </section>
              </td>
              <td style={styles.tableTd}>
                <section style={styles.input}>
                  <label>Width</label>
                  <label className="input">
                    <input
                      type="text"
                      name="width"
                      className="form-control input-xs"
                      placeholder="inches"
                      onChange={this.handleInputChange}
                      size="6"
                    />
                  </label>
                </section>
              </td>
              <td style={styles.tableTd}>
                <section style={styles.input}>
                  <label>Height</label>
                  <label className="input">
                    <input
                      type="text"
                      name="height"
                      className="form-control input-xs"
                      placeholder="inches"
                      onChange={this.handleInputChange}
                      size="6"
                    />
                  </label>
                </section>
              </td>
              <td style={styles.tableTd}>
                <section style={styles.input}>
                  <label>Weight</label>
                  <label className="input">
                    <input
                      type="text"
                      name="weight"
                      className="form-control input-xs"
                      placeholder="lbs"
                      onChange={this.handleInputChange}
                      size="6"
                    />
                  </label>
                </section>
              </td>
              <td style={styles.tableTd}>
                <button
                  className="btn btn-info btn-sm"
                  onClick={this.showDensityAndClass}
                >
                  Calculate
                </button>

              </td>
            </tr>
            <tr>
              <td colSpan="2">
                Density (lbs per cubic foot):{' '}
                <big><b>{this.state.density}</b></big>
              </td>
              <td colSpan="2">
                Recommended Freight Class:{' '}
                <big><b>{this.state.freightClass}</b></big>
              </td>
              <td />
            </tr>
          </tbody>
        </table>

      </div>
    );
  }

  handleInputChange = e => {
    this.inputs[e.target.name] = e.target.value;
  };

  showDensityAndClass = e => {
    e.target.blur();
    e.preventDefault();
    const { length = 0, width = 0, height = 0, weight = 0 } = this.inputs;
    this.setState(this.calculateDensityAndClass(length, width, height, weight));
  };

  calculateDensityAndClass(length, width, height, weight) {
    const cubicFeet = +length / 12 * (+width / 12) * (+height / 12);
    const density = Math.round(+weight / cubicFeet * 100) / 100;
    const freightClass = this.getFreightClass(density);

    return {
      freightClass,
      density,
    };
  }

  getFreightClass(density) {
    if (density < 1) {
      return '500';
    } else if (density < 2) {
      return '400';
    } else if (density < 3) {
      return '300';
    } else if (density < 4) {
      return '250';
    } else if (density < 5) {
      return '200';
    } else if (density < 6) {
      return '175';
    } else if (density < 7) {
      return '150';
    } else if (density < 8) {
      return '125';
    } else if (density < 9) {
      return '110';
    } else if (density < 10.5) {
      return '100';
    } else if (density < 12) {
      return '92.5';
    } else if (density < 13.5) {
      return '85';
    } else if (density < 15) {
      return '77.5';
    } else if (density < 22.5) {
      return '70';
    } else if (density < 30) {
      return '65';
    } else if (density < 35) {
      return '60';
    } else if (density < 50) {
      return '55';
    } else {
      return '50';
    }
  }
}

export default ClassDensityCalculator;
