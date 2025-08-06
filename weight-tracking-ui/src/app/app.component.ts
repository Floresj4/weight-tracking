import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from './components/header/header';

import { SAMPLE_ENTRY_DATA, SAMPLE_STAT_DATA } from './components/weight-entries/model/weight-entries.data';
import { WeightTable } from './components/weight-entries/weight-table/weight-table';
import { WeightEntry } from './components/weight-entries/model/weight-entry.model';
import { WeightStat as WeightStatModel } from './components/weight-entries/model/weight-stat.model';
import { WeightEntryNew } from './components/weight-entries/weight-entry-new/weight-entry-new';
import { WeightStat } from "./components/weight-entries/weight-stat/weight-stat";

import { SAMPLE_USERS_DATA } from './components/user/model/users.data';
import { UserModel } from './components/user/model/user.model';
import { User } from "./components/user/user";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Header, User, WeightTable, WeightEntryNew, WeightStat],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class App {

  selectedEntry = signal<WeightEntry>(<WeightEntry>{})

  weightEntries: WeightEntry[] = SAMPLE_ENTRY_DATA;
  weightStats: WeightStatModel[] = SAMPLE_STAT_DATA;

  selectedUser = signal<UserModel>(<UserModel>{})

  showNewEntry: boolean = false;

  constructor() {
    this.selectedUser.set(SAMPLE_USERS_DATA[0])
  }

  get averageWeight(): WeightStatModel {
    return this.weightStats[0]
  }

  get lowestWeight(): WeightStatModel {
    return this.weightStats[1]
  }

  get highestWeight(): WeightStatModel {
    return this.weightStats[2]
  }

  onAddNewEntry() {
    this.showNewEntry = true
  }

  onNewEntry(entry: WeightEntry) {
    this.showNewEntry = false
    console.log("New entry added: ", entry);
    this.weightEntries.unshift(entry)
  }

  onCloseNewEntry() {
    this.showNewEntry = false
  }

  onSelectedUser(id: string) {
    console.log("User selected: " + id)
  }
}
