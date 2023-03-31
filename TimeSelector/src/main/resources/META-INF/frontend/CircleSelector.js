/*
 * Copyright 2020 mgrankvi
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
import {PolymerElement} from '@polymer/polymer/polymer-element.js';
import {html} from '@polymer/polymer/lib/utils/html-tag.js';

class CircleSelector extends PolymerElement {
  static get template() {
    return html`
        <style>
            :host {
                --circle-stroke: gray;
                --circle-inside: white;
                --circle-selector: mediumaquamarine;
                --circle-selector-dot: seagreen;
                --circle-text: black;
            }
        </style>
        
        <canvas on-mousemove="_onMouseMove" on-mouseout="_onMouseOut" on-click="_onSelect"
                id="canvas"></canvas>
        `;
  }

  static get is() {
    return 'circle-selector'
  }

  static get properties() {
    return {
      numbers: {
        type: Array,
        value: []
      },
      values: {
        type: Array,
        value: []
      },
      innerValues: {
        type: Array,
        value: []
      },
      selection: {type: Number, value: 12},
      actualSelection: {type: Number, value: 12, notify: true},
      selectionVal: {type: Number, value: 12},
      sectors: {type: Number, value: 12},
      numSlices: {type: Number, value: 24},
      circleX: {type: Number, value: 125},
      circleY: {type: Number, value: 125},
      radian: {type: Number, value: 120},
      halfWidth: {type: Number, value: 10}
    }
  }

  ready() {
    super.ready();
    this._render();
    this.addEventListener('actual-selection-changed', this._paintSelection);
  }

  _paintSelection() {
    this.selection = this.actualSelection;
    this._render();
  }

  get ctx() {
    if (!this.__ctx) {
      this.__ctx = this.$.canvas.getContext("2d");
    }
    return this.__ctx;
  }

  _render() {
    let ctx = this.ctx;
    if (!ctx)
      return;

    let styles = window.getComputedStyle(this, ":host");

    let canvas = this.$.canvas;
    canvas.width = 250;
    canvas.height = 250;

    ctx.fillStyle = styles.getPropertyValue("--circle-stroke");
    ctx.beginPath();
    ctx.arc(this.circleX, this.circleY, this.radian, 0, 2 * Math.PI, false);
    ctx.closePath();
    ctx.stroke();
    ctx.fill();

    // Fill inside leaving only outside stroke.
    ctx.fillStyle = styles.getPropertyValue("--circle-inside");
    ctx.beginPath();
    ctx.arc(this.circleX, this.circleY, this.radian, 0, 2 * Math.PI, false);
    ctx.closePath();
    ctx.fill();

    // Center dot
    ctx.fillStyle = styles.getPropertyValue("--circle-stroke");
    ctx.beginPath();
    ctx.arc(this.circleX, this.circleY, 1, 0, 2 * Math.PI, false);
    ctx.closePath();
    ctx.stroke();
    ctx.fill();

    let textMetrics = ctx.measureText("00");
    let textWidth = Math.ceil(textMetrics.width);
    this.halfWidth = textWidth / 2;

    if (this.numbers.length === 0) {
      this._calculateNumbers();
    }

    if (this.selection !== null) {
      // ctx.fillStyle = "gainsboro";

      let degrees = (this.selection * (360 / (this.numSlices / 2)));
      let rad = (degrees * Math.PI / 180) - (0.5 * Math.PI);

      ctx.strokeStyle = styles.getPropertyValue("--circle-selector");
      ctx.beginPath();
      ctx.moveTo(this.circleX, this.circleY);
      let radDistance = this.radian - 15;
      if (this.innerValues.length > 0 && this._contains(this.innerValues, this.selection)) {
        radDistance = this.radian / 2;
      }
      let x = this.circleX + (Math.cos(rad) * radDistance);
      let y = this.circleY + (Math.sin(rad) * radDistance);
      ctx.lineTo(x, y);
      ctx.closePath();
      ctx.stroke();

      ctx.beginPath();
      ctx.fillStyle = styles.getPropertyValue("--circle-selector");
      ctx.arc(x, y, Math.sqrt(textWidth * textWidth + 15 * 15) / 2, 0, 2 * Math.PI, false);
      ctx.closePath();
      ctx.fill();

      if(this._noNumber()) {
        ctx.beginPath();
        ctx.fillStyle = styles.getPropertyValue("--circle-selector-dot");
        ctx.arc(x, y, 2, 0, 2 * Math.PI, false);
        ctx.closePath();
        ctx.fill();
      }
    }

    ctx.strokeStyle = styles.getPropertyValue("--circle-text");
    ctx.fillStyle = styles.getPropertyValue("--circle-text");

    for (var i = 0; i < this.numbers.length; i++) {
      let number = this.numbers[i];
      ctx.fillText(number.text, number.x, number.y);
    }
  }

  _noNumber() {
    if(this.selection == this.sectors) {
      // If selection is on the first slice
      return false;
    }
    for (var i = 0; i < this.numbers.length; i++) {
      let number = this.numbers[i];
      if(this.selection == number.text) {
        // If selection is on a rendered number
        return false;
      }
    }
    return true;
  }

  _onSelect(event) {
    this.actualSelection = this.selection;
  }

  _onMouseMove(event) {
    let relativeX = event.pageX - this.$.canvas.offsetLeft;
    let relativeY = event.pageY - this.$.canvas.offsetTop;

    this._moveEvent(relativeX, relativeY);
  }

  _moveEvent(relativeX, relativeY) {
    // inside circle
    let relX = relativeX - this.circleX;
    let relY = relativeY - this.circleY;
    if (relX * relX + relY * relY <= this.radian * this.radian) {
      // + 0.5PI so we get 0 up top at 12 o'clock on the circle instead of 3 o'clock
      let theta = Math.atan2(relativeY - this.circleY, relativeX - this.circleX) + (Math.PI / 2);
      //theta is now in the range -Math.PI to Math.PI
      if (theta < 0)
        theta = 2 * Math.PI + theta;

      //Now theta is in the range [0, 2*pi]
      //Use this value to determine which slice of the circle the point resides in.
      //For example:
      var whichSlice = 0;
      let sliceSize = Math.PI * 2 / this.numSlices;
      var sliceStart;
      for (var i = 1; i <= this.numSlices; i++) {
        sliceStart = i * sliceSize;

        if (theta < sliceStart) {
          whichSlice = i;
          break;
        }
      }

      let number = Math.floor(whichSlice / 2);

      // Special case for 1-12 hour clock
//            if (number == 0 && numSlices == 24) number = 12;
      if (number === 0) {
        number = this.sectors;
      }

      let distance = Math.sqrt(Math.pow(this.circleX - relativeX, 2) + Math.pow(this.circleY - relativeY, 2));
      if (this.innerValues.length > 0 && this.innerValues.length == this.sectors && distance <= (this.radian / 2) + this.halfWidth) {
        number = this.innerValues[number - 1];
        this.selectionVal = number;
      } else if (this.sectors == this.values.length) {
        // If we have as many values as sectors use the value for number.
        this.selectionVal = this.values[number - 1];
      } else {
        this.selectionVal = number;
      }

      this.selection = number;
      // circleSelectCallback valueHover(selectionValue);
    } else {
      // circleSelectCallback method mouse out!
      this._onMouseOut();
    }
    this._render();
  }

  _onMouseOut(event) {
    this.selection = this.actualSelection;
    this._render();
  }

  _contains(list, value) {
    for (var i = 0; i < list.length; i++) {
      if (list[i] == value) {
        return true;
      }
    }
    return false;
  }

  _calculateNumbers() {
    let ctx = this.ctx;

    let degreesPerStep = 360 / this.values.length;
    let textMetrics = ctx.measureText("0123456789");
    let textHeight = textMetrics.actualBoundingBoxAscent + textMetrics.actualBoundingBoxDescent;
    let textHalfHeight = textHeight / 2;

    for (var i = 1; i <= this.values.length; i++) {
      let halfWidth = Math.ceil(ctx.measureText(this.values[i - 1]).width) /2;

      let degrees = i * degreesPerStep;
      let rad = (degrees * Math.PI / 180) - (0.5 * Math.PI);

      let x = this.circleX + (Math.cos(rad) * (this.radian - 15)) - halfWidth;
      let y = this.circleY + (Math.sin(rad) * (this.radian - 15)) + textHalfHeight;

      this.numbers.push({
        text: this.values[i - 1],
        x: x,
        y: y
      });
    }
    if (this.innerValues.length > 0) {
      for (var i = 1; i <= this.innerValues.length; i++) {
        let halfWidth = Math.ceil(ctx.measureText(this.innerValues[i - 1]).width) /2;
        let degrees = i * degreesPerStep;
        let rad = (degrees * Math.PI / 180) - (0.5 * Math.PI);

        let x = this.circleX + (Math.cos(rad) * (this.radian / 2)) - halfWidth;
        let y = this.circleY + (Math.sin(rad) * (this.radian / 2)) + textHalfHeight;

        this.numbers.push({
          text: this.innerValues[i - 1],
          x: x,
          y: y
        });
      }
    }
  }
}

window.customElements.define(CircleSelector.is, CircleSelector);

export { CircleSelector }
