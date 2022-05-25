import { Component } from './Component'
import { AuthStatus } from '../types/AuthStatus'
import { doRequest } from '../utils/Request'

export class LogoutButton extends Component {
  buttonElement!: HTMLButtonElement
  isLogged = false

  mounted(): void {
    this.buttonElement = document.createElement('button')
    this.buttonElement.classList.add('btn', 'btn-outline-danger')
    this.buttonElement.innerText = 'Logout'
    this.buttonElement.onclick = async () => {
      // TODO: Do logout

      try {
        await doRequest('/logout', 'POST')
      } catch (error) {
        console.error('Error while logging out?')
      }

      this.notifySubscribers('logout', {
        isLogged: false,
        username: '',
      } as AuthStatus)

      this.isLogged = false
      this.update()
    }

    this.mountElement.appendChild(this.buttonElement)
  }

  showState(): void {
    this.buttonElement.hidden = !this.isLogged
  }
}
