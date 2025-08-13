import { Component, computed, input, output } from '@angular/core';

import { UserModel } from './model/user.model';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [],
  templateUrl: './user.component.html',
  styleUrl: './user.component.scss'
})
export class UserComponent {

    user = input.required<UserModel>()

    imagePath = computed(() => '/' + this.user().avatar )

    selected = output<string>()
    
    onSelect() {
      this.selected.emit(this.user().id)
    }
}