import { AuthStatus } from '../types/AuthStatus'
import { doRequest } from '../utils/Request'
import { Component } from './Component'
import { LoadingPage } from './pages/LoadingPage'
import { Page } from './pages/Page'
import { AuthPage } from './pages/AuthPage'
import { AlbumPage } from './pages/AlbumPage'

export class Router extends Component {
  currentPage!: Page

  mounted() {
    this.getAlbumPage()
  }

  private getAlbumPage() {
    this.updateCurrentPage(new LoadingPage(this.mountElement))

    doRequest('/albums', 'GET')
      .then(async (response) => {
        const responseData = await response.json()
        this.setAlbumPage(responseData)
        this.notifySubscribers('logged')
      })
      .catch((error) => {
        console.error(error)
        this.setAuthPage()
      })
  }

  private updateCurrentPage(newPage: Page) {
    this.currentPage = newPage
    newPage.mount()
    this.update()
  }

  private setAuthPage() {
    const authPage = new AuthPage(this.mountElement)
    authPage.addSubscriber('logged', (event) => this.updateAuthStatus(event))

    this.updateCurrentPage(authPage)
  }

  private setAlbumPage(allAlbums: any) {
    const albumPage = new AlbumPage(this.mountElement)
    this.updateCurrentPage(albumPage)
  }

  showState(): void {
    this.currentPage.update()
  }

  updateAuthStatus(authStatus: AuthStatus) {
    console.debug(authStatus)
    if (authStatus.isLogged) {
      this.notifySubscribers('logged')
      this.getAlbumPage()
    } else {
      this.setAuthPage()
    }
  }
}
