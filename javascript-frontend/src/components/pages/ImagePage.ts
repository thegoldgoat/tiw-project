import { doRequest } from '../../utils/Request'
import { CommentsList } from '../CommentsList'
import { eventBus } from '../EventBus'
import { ImageComponent } from '../ImageComponent'
import { ToastMessage } from '../ToastComponent'
import { LoadingPage } from './LoadingPage'
import { Page } from './Page'

export class ImagePage extends Page {
  private imagePk: number
  private isLoading = true
  private loadingPage!: LoadingPage

  private imageComponent!: ImageComponent
  private commentsList!: CommentsList

  private afterLoadingDiv!: HTMLElement
  private imageComponentMount!: HTMLDivElement
  private commentsMount!: HTMLDivElement

  constructor(mountElement: HTMLElement, imagePk: number) {
    super(mountElement)
    this.imagePk = imagePk
  }

  protected mounted() {
    this.loadingPage = new LoadingPage(this.mountElement)

    this.afterLoadingDiv = document.createElement('div')
    this.imageComponentMount = document.createElement('div')
    this.commentsMount = document.createElement('div')

    this.afterLoadingDiv.appendChild(this.imageComponentMount)
    this.afterLoadingDiv.appendChild(this.commentsMount)

    this.commentsList = new CommentsList(this.commentsMount, this.imagePk)

    this.commentsList.addSubscriber('updatecomments', () => this.getFromAPI())

    this.getFromAPI()
  }

  private async getFromAPI() {
    this.isLoading = true
    this.update()
    try {
      const response = await doRequest(`/image?imagePk=${this.imagePk}`, 'GET')
      const responseJson = await response.json()

      this.imageComponent = new ImageComponent(
        this.imageComponentMount,
        responseJson.image,
        false,
        false
      )
      this.imageComponent.mount()

      this.commentsList.comments = responseJson.comments
      this.commentsList.mount()
    } catch (error) {
      eventBus.notifySubscribers('toast', {
        isError: true,
        message: error,
      } as ToastMessage)
    }
    this.mountElement.innerHTML = 'ue'
    this.isLoading = false
    this.update()
  }

  protected showState(): void {
    if (this.isLoading) {
      this.loadingPage.mount()
    } else {
      this.mountElement.innerHTML = ''
      this.mountElement.appendChild(this.afterLoadingDiv)
    }
  }
}
