import { Component, computed, input, output } from '@angular/core';

import { User } from './model/user.model';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [],
  templateUrl: './user.component.html',
  styleUrl: './user.component.scss'
})
export class UserComponent {

    user = input.required<User>()

    imagePath = computed(() => '/' + this.user().avatar )

    selected = output<string>()
    
    onSelect() {
      this.selected.emit(this.user().id)
    }
}