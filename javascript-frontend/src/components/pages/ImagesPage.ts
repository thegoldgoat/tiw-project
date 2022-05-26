import {doRequest} from '../../utils/Request'
import {LoadingPage} from './LoadingPage'
import {Page} from './Page'

export class ImagesPage extends Page {
  private albumPk: number
  private currentPage: number

  private isLoading = true

  private loadingPage!: LoadingPage

  constructor(mountElement: HTMLElement, albumPk: number) {
    super(mountElement)
    this.albumPk = albumPk
    this.currentPage = 0
  }

  protected async mounted() {
    this.loadingPage = new LoadingPage(this.mountElement)
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
      console.debug(await response.json())
    } catch (error) {
      console.error(error)
    }

    this.isLoading = false
    this.update()
  }

  protected showState(): void {
    if (this.isLoading) {
      this.loadingPage.mount()
    } else {
      this.mountElement.innerHTML = 'Response in console'
    }
  }
}
