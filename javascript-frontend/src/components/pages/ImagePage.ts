import { doRequest } from '../../utils/Request'
import { LoadingPage } from './LoadingPage'
import { Page } from './Page'

export class ImagePage extends Page {
  private imagePk: number
  private isLoading = true
  private loadingPage!: LoadingPage

  constructor(mountElement: HTMLElement, imagePk: number) {
    super(mountElement)
    this.imagePk = imagePk
  }

  protected mounted() {
    this.loadingPage = new LoadingPage(this.mountElement)
    this.getFromAPI()
  }

  private async getFromAPI() {
    this.isLoading = true
    this.update()
    try {
      const response = await doRequest(`/image?imagePk=${this.imagePk}`, 'GET')
      console.debug(await response.json())
    } catch (error) {
      console.error(error)
    }
    this.mountElement.innerHTML = 'ue'
    this.isLoading = false
    this.update()
  }

  protected showState(): void {
    if (this.isLoading) {
      this.loadingPage.mount()
    } else {
      this.mountElement.innerHTML = 'Image Page'
    }
  }
}
