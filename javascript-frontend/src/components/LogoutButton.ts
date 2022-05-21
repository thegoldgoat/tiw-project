import { Component } from './Component'
import { AuthStatus } from '../types/AuthStatus'

export class LogoutButton extends Component {
  buttonElement!: HTMLButtonElement
  isLogged = true

  mounted(): void {
    this.buttonElement = document.createElement('button')
    this.buttonElement.classList.add('btn', 'btn-outline-danger')
    this.buttonElement.innerText = 'Logout'
    this.buttonElement.onclick = () => {
      // TODO: Do logout
      this.notifySubscribers('logout', {
        isLogged: false,
        username: '',
      } as AuthStatus)

      this.isLogged = false
      this.update()
    }
  }

  showState(): void {
    if (this.isLogged) this.mountElement.appendChild(this.buttonElement)
  }
}
