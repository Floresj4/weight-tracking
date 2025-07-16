import { Component, input } from '@angular/core';
import { WeightEntry } from '../../../model/weight-entry.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-weight',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './weight.html',
  styleUrl: './weight.scss'
})
export class Weight {

  entry = input.required<WeightEntry>()
}
