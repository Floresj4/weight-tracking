import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from './components/header/header.component';

import { WeightTable } from './components/weight-entries/weight-table/weight-table.component';
import { WeightEntry } from './components/weight-entries/model/weight-entry.model';
import { WeightStat as WeightStatModel } from './components/weight-entries/model/weight-stat.model';
import { WeightEntryNew } from './components/weight-entries/weight-entry-new/weight-entry-new';
import { WeightStatComponent } from "./components/weight-entries/weight-stat/weight-stat.component";

import { UserModel } from './components/user/model/user.model';
import { User } from "./components/user/user";
import { WeightEntryService } from './components/weight-entries/weight-entry.service';
import { UserService } from './components/user/user.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Header, User, WeightTable, WeightEntryNew, WeightStatComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class App {

  weightEntries: WeightEntry[] = []

  selectedUser = signal<UserModel>(<UserModel>{})

  showNewEntry: boolean = false;

  constructor(private userService: UserService,
    private weightEntryService: WeightEntryService) {

    const user = this.userService.getUserById("1")
    if(user) {
      this.selectedUser.set(user)
      this.weightEntries = this.weightEntryService.getEntries();
    }
  }

  get averageWeight(): WeightStatModel {
    return this.weightEntryService.getAverageEntry();
  }

  get lowestWeight(): WeightStatModel {
    return this.weightEntryService.getLowestEntry()
  }

  get highestWeight(): WeightStatModel {
    return this.weightEntryService.getHighestEntry()
  }

  onAddNewEntry() {
    this.showNewEntry = true
  }

  onNewEntry(entry: WeightEntry) {
    this.showNewEntry = false

    console.log("New entry added: ", entry)
    this.weightEntries.unshift(entry)
  }

  onCloseNewEntry() {
    this.showNewEntry = false
  }

  onSelectedUser(id: string) {
    console.log("User selected: " + id)
  }
}
