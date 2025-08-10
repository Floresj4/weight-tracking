import { Component, input } from '@angular/core';

import { WeightEntry } from '../model/weight-entry.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-weight-table',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './weight-table.component.html',
  styleUrl: './weight-table.component.scss'
})
export class WeightTable {

  entries = input.required<WeightEntry[]>()

  constructor() {
  }

  get entriesList() {
    return this.entries();
  }
}
