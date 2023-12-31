import React, { Component } from 'react'
import ReactDOM from 'react-dom'
const $ = window.jQuery;

class EasyPieChartContainer extends Component {
    componentDidMount() {
        $('.easy-pie-chart', $(ReactDOM.findDOMNode(this))).each(function(idx, element) {
            var $this = $(element),
                barColor = $this.css('color') || $this.data('pie-color'),
                trackColor = $this.data('pie-track-color') || 'rgba(0,0,0,0.04)',
                size = parseInt($this.data('pie-size'), 10) || 25;

            $this.easyPieChart({

                barColor : barColor,
                trackColor : trackColor,
                scaleColor : false,
                lineCap : 'butt',
                lineWidth : parseInt(size / 8.5, 10),
                animate : 1500,
                rotate : -90,
                size : size,
                onStep: function(from, to, percent) {
                    $(this.el).find('.percent').text(Math.round(percent));
                }

            });
            $this.find('canvas').attr('data-reactid', $this.data('reactid') + '.0.1')
        });
    };

    render() {
        return (
            <div className={this.props.className}>
                {this.props.children}
            </div>
        )
    };
};

export default EasyPieChartContainer
