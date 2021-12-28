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
import {CircleSelector} from "./CircleSelector";


export class PopupSelector extends PolymerElement {
  static get template() {
    return html`

        <style type="text/css">
            :host {
                --background-color: floralwhite;
                --top-color: mediumaquamarine;
                --selector-color: lightseagreen;
                --selected-color: white;
                --selector-font-size: 40px;
            }
            .popup {
                width: 275px;

                border: 1px solid black;
            }

            .overlay {
                position: absolute;
                top: 0;
                left: 0;

                width: 100vw;
                height: 100vh;

                display: flex;
                align-items: center;
                justify-content: center;
            }

            .content {
                display: flex;
                flex-flow: column;
                text-align: center;
                justify-content: space-around;

                background-color: var(--background-color);
            }

            .time-label {
                font-weight: bold;
                font-size: 21px;

                padding: 0 10px;

            }

            .selector-popup-top {
                background-color: var(--top-color);
                margin-bottom: 10px;
            }

            .selector-number {
                color: var(--selector-color);
                font-size: var(--selector-font-size);
            }

            .selector-number.selected {
                color: var(--selected-color);
            }

            .select-button {
                background-color: transparent;
                border: none;
                color: blue;
                font-weight: bold;
                font-size: 15px;
            }

            .select-button:focus {
                outline: none;
            }
            
            .am-pm-labels{
                font-size: calc(var(--selector-font-size) * 0.4);
                padding-left: 10px;
            }
            .am-pm.selected {
                color: var(--selected-color);
            }

        </style>

        <div class="overlay">
        <div class="popup">
            <div class="content">
                <div class="time-label selector-popup-top" style="display: flex; justify-content: center;">
                    <!-- Labels will create format as "00:00" -->
                    <label class="selector-number" id="hourLabel" on-click="_initHours">[[hours]]</label>
                    <label class="selector-number">:</label>
                    <label class="selector-number" id="minuteLabel" on-click="_initMinutes">[[minutes]]</label>
                    <template id="am-pm-if" is="dom-if" if="{{!twentyFour}}">
                        <div class="am-pm-labels" style="display: flex; flex-direction: column; justify-content: center; ">
                          <label class="am-pm" id="amLabel">AM</label>
                          <label class="am-pm" id="pmLabel">PM</label>
                        </div>
                    </template>
                </div>
                <slot></slot>
                <span style="align-self: flex-end; padding: 5px;">
                    <button class="select-button" on-click="_cancelAndClose">Cancel</button>
                    <button class="select-button" on-click="_selectAndClose">Ok</button>
                </span>
            </div>
        </div>
        </div>
        `;
  }

  static get is() {
    return 'popup-selector'
  }

  static get properties() {
    return {
      hours: {
        type: String
      },
      minutes: {
        type: String
      },
      showingHours: {
        type: Boolean,
        value: true
      },
      updating: {
        type: Boolean,
        value: false
      },
      selector: {
        type: Object,
        value: new CircleSelector()
      },
      twentyFour: {
        type: Boolean
      }
    }
  }

  ready() {
    super.ready();
    this._initializeCircle();
    this.selector.addEventListener('actual-selection-changed', this._selection.bind(this));
  }

  _initializeCircle() {
    this._initHours();

    this.appendChild(this.selector);
  }

  _selection() {
    if (this.updating) {
      return;
    }

    if (this.showingHours) {
      this._initMinutes();
      this._populateString();
    } else {
      this.minutes = this.selector.actualSelection;
      this._populateString();
    }
  }

  _populateString() {
    if (this.hours == 24) {
      this.hours = 0;
    }
    if (this.minutes == 60) {
      this.minutes = 0;
    }
    this.hours = (this.hours + '');
    this.minutes = (this.minutes + '');
    if (this.hours.length < 2) {
      this.hours = '0' + this.hours;
    }
    if (this.minutes.length < 2) {
      this.minutes = '0' + this.minutes;
    }
  }

  _initHours() {
    this.selector.values = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
    if(this.twentyFour) {
      this.selector.innerValues = [13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24];
    }else {
      // Timing issue can not find element
      // if (this.hours >= 12) {
      //   this.shadowRoot.querySelector('#amLabel').classList.remove("selected");
      //   this.shadowRoot.querySelector('#pmLabel').classList.add("selected");
      // } else {
      //   this.shadowRoot.querySelector('#amLabel').classList.add("selected");
      //   this.shadowRoot.querySelector('#pmLabel').classList.remove("selected");
      // }
    }
    this.selector.numbers = [];
    this.selector.numSlices = 24;

    this.updating = true;
    this.selector.selection = this.selector.actualSelection = this.hours;
    this.updating = false;

    this.showingHours = true;

    this.$.hourLabel.classList.add("selected");
    this.$.minuteLabel.classList.remove("selected");
  }

  _initMinutes() {
    if (!this.showingHours) {
      return;
    }

    this.selector.values = [5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 0];
    this.selector.innerValues = [];
    this.selector.numbers = [];
    this.selector.numSlices = 120;
    this.hours = this.selector.actualSelection;

    this.updating = true;
    this.selector.selection = this.selector.actualSelection = this.minutes;
    this.updating = false;

    this.showingHours = false;

    this.$.hourLabel.classList.remove("selected");
    this.$.minuteLabel.classList.add("selected");
  }

  _selectAndClose() {
    this.dispatchEvent(new CustomEvent('values', {
      detail: {
        hours: this.hours,
        minutes: this.minutes,
      }
    }));
    document.body.removeChild(this);
  }

  _cancelAndClose() {
    document.body.removeChild(this);
  }
}

window.customElements.define(PopupSelector.is, PopupSelector);

