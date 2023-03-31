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
import { PolymerElement } from '@polymer/polymer/polymer-element.js';
import { html } from '@polymer/polymer/lib/utils/html-tag.js';

import {PopupSelector} from "./PopupSelector";


class TimeSelector extends PolymerElement {
  static get template() {
    return html`
        <style>
            :host {
                --selector-font-size: 20px;
            }
            .time-selector {
                font-size: var(--selector-font-size);
            }
        </style>

        <label class="time-selector" on-click="_openCircle">[[time]]</label>`;
  }

  static get is() {
    return 'time-selector'
  }

  static get properties() {
    return {
      value: {
        type: String,
        notify: true
      },
      time: {
        type: String,
        notify: true
      },
      open: {
        type: Boolean,
        value: false
      },
      hours: {
        type: Boolean,
        value: true
      },
      updating: {
        type: Boolean,
        value: false
      },
      twentyFour: {
        type: Boolean,
        value: true,
        notify: true
      }
    }
  }

  ready() {
    super.ready();
    this.time = this.value;
    this.addEventListener('value-changed', (value) => this.time = value);
    this.addEventListener('twenty-four-changed', () => this._populateString(this._parseHours(), this._parseMinutes()));
  }

  _openCircle() {
    const popup = new PopupSelector();
    document.body.appendChild(popup);
    popup.hours = this._parseHours();
    popup.minutes = this._parseMinutes();
    popup.twentyFour = this.twentyFour;
    popup._populateString();
    popup._initializeCircle();


    popup.addEventListener('values', function (event) {
      this._valueChange(event)
    }.bind(this));
  }

  _valueChange(event) {
    if (event.detail) {
      this._populateString(event.detail.hours, event.detail.minutes);
    } else {
      console.log(event);
    }
  }

  // Parse the current minute value from the value string
  _parseMinutes() {
    return this.value.substring(this.value.indexOf(":") + 1, this.value.length);
  }

  // Parse the current hour value from the value string
  _parseHours() {
    return this.value.substring(0, this.value.indexOf(":"));
  }

  _populateString(hours, minutes) {
    if (hours.length < 2) {
      hours = '0' + hours;
    }
    if (minutes.length < 2) {
      minutes = '0' + minutes;
    }
    this.value = hours + ':' + minutes;
    if(this.twentyFour) {
      this.time = hours + ':' + minutes;
    } else {
      let amPm = hours > 12 ? 'pm' : 'am';
      let hourString = Number(hours) > 12 ? Number(hours) - 12 : hours;
      if (hourString.length < 2) {
        hourString = '0' + hourString;
      }
      this.time = hourString + ':' + minutes + amPm;
    }
  }
}

window.customElements.define(TimeSelector.is, TimeSelector);

