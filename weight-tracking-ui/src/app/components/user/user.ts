import { Component, computed, input, output } from '@angular/core';

import { UserModel } from './model/user.model';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [],
  templateUrl: './user.html',
  styleUrl: './user.scss'
})
export class User {

    user = input.required<UserModel>()

    imagePath = computed(() => '/' + this.user().avatar )

    selected = output<string>()
    
    onSelect() {
      this.selected.emit(this.user().id)
    }
}