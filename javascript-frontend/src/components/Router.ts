import { AuthStatus } from '../types/AuthStatus'
import { doRequest } from '../utils/Request'
import { Component } from './Component'
import { LoadingPage } from './pages/LoadingPage'
import { Page } from './pages/Page'
import { AuthPage } from './pages/AuthPage'
import { AlbumPage } from './pages/AlbumPage'
import { AllAlbums } from '../types/AllAlbums'
import { eventBus } from './EventBus'
import { ImagesPage } from './pages/ImagesPage'
import { ImagePage } from './pages/ImagePage'

export class Router extends Component {
  currentPage!: Page
  isLogged = false
  allAlbum!: AllAlbums

  mounted() {
    this.getAlbumPage()
  }

  private getAlbumPage() {
    this.updateCurrentPage(new LoadingPage(this.mountElement))

    doRequest('/albums', 'GET')
      .then(async (response) => {
        const responseData: AllAlbums = await response.json()
        this.isLogged = true
        this.allAlbum = responseData
        this.setAlbumPage()
        eventBus.notifySubscribers('receivedAlbums')
      })
      .catch((error) => {
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

    this.updateCurrentPage(authPage)
  }

  private setAlbumPage() {
    const albumPage = new AlbumPage(this.mountElement)
    albumPage.allAlbums = this.allAlbum
    this.updateCurrentPage(albumPage)
  }

  private setImagePage(imagePk: number) {
    this.updateCurrentPage(new ImagePage(this.mountElement, imagePk))
  }

  showState(): void {
    this.currentPage.update()
  }

  updateAuthStatus(authStatus: AuthStatus) {
    this.isLogged = authStatus.isLogged
    if (authStatus.isLogged) {
      this.getAlbumPage()
    } else {
      this.setAuthPage()
    }
  }

  openAlbum(albumPk: number) {
    this.updateCurrentPage(new ImagesPage(this.mountElement, albumPk))
  }

  openImage(imagePk: number) {
    this.setImagePage(imagePk)
  }

  gotoHome() {
    if (this.isLogged) {
      if (this.allAlbum) {
        this.setAlbumPage()
      } else {
        this.getAlbumPage()
      }
    } else {
      this.setAuthPage()
    }
  }
}
