import { Component, input } from '@angular/core';

import { WeightEntry } from '../../model/weight-entry.model';

@Component({
  selector: 'app-weight-table',
  standalone: true,
  imports: [ ],
  templateUrl: './weight-table.html',
  styleUrl: './weight-table.scss'
})
export class WeightTable {

  entries = input.required<WeightEntry[]>()
}
