import {AuthStatus} from '../types/AuthStatus'
import {doRequest} from '../utils/Request'
import {Component} from './Component'
import {LoadingPage} from './pages/LoadingPage'
import {Page} from './pages/Page'
import {AuthPage} from './pages/AuthPage'
import {AlbumPage} from './pages/AlbumPage'
import {AllAlbums} from '../types/AllAlbums'
import {eventBus} from './EventBus'
import {ImagesPage} from './pages/ImagesPage'

export class Router extends Component {
    currentPage!: Page

    mounted() {
        this.getAlbumPage()
    }

    private getAlbumPage() {
        this.updateCurrentPage(new LoadingPage(this.mountElement))

        doRequest('/albums', 'GET')
            .then(async (response) => {
                const responseData: AllAlbums = await response.json()
                this.setAlbumPage(responseData)
                eventBus.notifySubscribers('receivedAlbums')
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

    this.updateCurrentPage(authPage)
  }

    private setAlbumPage(allAlbums: AllAlbums) {
        const albumPage = new AlbumPage(this.mountElement)
        albumPage.allAlbums = allAlbums
        this.updateCurrentPage(albumPage)
    }

    showState(): void {
        this.currentPage.update()
    }

    updateAuthStatus(authStatus: AuthStatus) {
        console.debug(authStatus)
        if (authStatus.isLogged) {
            this.getAlbumPage()
        } else {
            this.setAuthPage()
        }
    }

    openAlbum(albumPk: number) {
        this.updateCurrentPage(new ImagesPage(this.mountElement, albumPk))
    }
}
