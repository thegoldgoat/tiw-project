import { AuthStatus } from '../types/AuthStatus'
import { doRequest } from '../utils/Request'
import { Component } from './Component'
import { LoadingPage } from './pages/LoadingPage'
import { Page } from './pages/Page'

export class Router extends Component {
  currentPage!: Page

  async mounted() {
    this.currentPage = new LoadingPage(this.mountElement)

    try {
      const respose = await doRequest('//google.com', 'GET', {})

      console.log(respose)
    } catch (error) {
      this.notifySubscribers('error', error)
    }
  }

  showState(): void {
    this.currentPage.update()
  }

  updateAuthStatus(authStatus: AuthStatus) {
    console.debug(authStatus)
  }
}
