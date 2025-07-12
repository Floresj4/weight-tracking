import { Component, computed, input, Output, EventEmitter } from '@angular/core';

import { UserModel } from '../../model/user.model';

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

    @Output() select: EventEmitter<string> = new EventEmitter<string>()

    onSelect() {
      this.select.emit(this.user().id)
    }
}