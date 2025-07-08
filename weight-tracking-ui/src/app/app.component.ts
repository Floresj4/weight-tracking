import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from './component/header/header';

import { SAMPLE_ENTRY_DATA } from './model/weight-entries.data';
import { WeightEntry } from './model/weight-entry.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Header],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class App {

  randomIndex: number = 0
  selectedEntry = signal<WeightEntry>(<WeightEntry>{})

  constructor() {
    this.onNewEntry()
  }

  onNewEntry() {
    this.randomIndex = Math.floor(Math.random() * SAMPLE_ENTRY_DATA.length)
    this.selectedEntry.set(SAMPLE_ENTRY_DATA[this.randomIndex])
  }
}
