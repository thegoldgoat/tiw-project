import { doRequest } from '../../utils/Request'
import { eventBus } from '../EventBus'
import { ImagesList } from '../ImagesList'
import { ToastMessage } from '../ToastComponent'
import { LoadingPage } from './LoadingPage'
import { Page } from './Page'

export class ImagesPage extends Page {
  private albumPk: number
  private currentPage: number

  private isLoading = true

  private loadingPage!: LoadingPage
  private imagesList!: ImagesList

  constructor(mountElement: HTMLElement, albumPk: number) {
    super(mountElement)
    this.albumPk = albumPk
    this.currentPage = 0
  }

  protected async mounted() {
    this.loadingPage = new LoadingPage(this.mountElement)
    this.imagesList = new ImagesList(this.mountElement, {
      currentPage: 0,
      images: [],
      pageCount: 0,
    })

    this.imagesList.addSubscriber('updatepage', (newPage: number) => {
      this.currentPage = newPage
      this.updatePage()
    })

    this.updatePage()
  }

  private async updatePage() {
    this.isLoading = true
    this.update()
    try {
      const response = await doRequest(
        `/album?albumPk=${this.albumPk}&page=${this.currentPage}`,
        'GET'
      )
      this.imagesList.allImages = await response.json()
    } catch (error) {
      eventBus.notifySubscribers('toast', {
        isError: true,
        message: error,
      } as ToastMessage)
    }

    this.isLoading = false
    this.update()
  }

  protected showState(): void {
    if (this.isLoading) {
      this.loadingPage.mount()
    } else {
      this.imagesList.mount()
    }
  }
}
