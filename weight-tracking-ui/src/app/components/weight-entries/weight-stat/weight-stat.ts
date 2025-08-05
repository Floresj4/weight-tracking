import { Component, input } from '@angular/core';
import { Card } from "../../components/shared/card/card";

@Component({
  selector: 'app-weight-stat',
  standalone: true,
  imports: [Card],
  templateUrl: './weight-stat.html',
  styleUrl: './weight-stat.scss'
})
export class WeightStat {

  label = input.required<string>()
  value = input.required<number>()
  date = input<string>()
}
