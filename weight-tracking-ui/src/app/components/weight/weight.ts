import { Component, input } from '@angular/core';
import { WeightEntry } from '../../model/weight-entry.model';

@Component({
  selector: 'app-weight',
  standalone: true,
  imports: [],
  templateUrl: './weight.html',
  styleUrl: './weight.scss'
})
export class Weight {

  entry = input.required<WeightEntry>()
}
