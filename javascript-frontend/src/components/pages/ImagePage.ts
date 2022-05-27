import { doRequest } from '../../utils/Request'
import { ImageComponent } from '../ImageComponent'
import { LoadingPage } from './LoadingPage'
import { Page } from './Page'

export class ImagePage extends Page {
  private imagePk: number
  private isLoading = true
  private loadingPage!: LoadingPage
  private imageComponent!: ImageComponent

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
      const responseJson = await response.json()
      this.imageComponent = new ImageComponent(
        this.mountElement,
        responseJson.image,
        false
      )
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
      this.imageComponent.mount()
    }
  }
}
