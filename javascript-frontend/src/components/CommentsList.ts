import { MyComment } from '../types/Comment'
import { doRequest } from '../utils/Request'
import { CommentComponent } from './CommentComponent'
import { Component } from './Component'
import { eventBus } from './EventBus'
import { ToastMessage } from './ToastComponent'

export class CommentsList extends Component {
  comments: MyComment[] = []
  imagePk: number

  newCommentElement!: HTMLElement
  commentInput!: HTMLInputElement
  separatorElement!: HTMLElement

  constructor(mountElement: HTMLElement, imagePk: number) {
    super(mountElement)
    this.imagePk = imagePk
  }

  protected mounted(): void {
    this.separatorElement = document.createElement('div')
    this.separatorElement.innerHTML = '<hr/><h3>Comments:</h3>'

    this.newCommentElement = document.createElement('form')

    const formGroup = document.createElement('div')
    formGroup.classList.add('form-group')

    this.commentInput = document.createElement('input')
    this.commentInput.classList.add('form-control')
    this.commentInput.placeholder = 'Write your comment'

    const buttonDiv = document.createElement('div')
    buttonDiv.classList.add('d-grid')
    buttonDiv.classList.add('gap-2')
    buttonDiv.classList.add('mt-4')

    const sendButton = document.createElement('button')
    sendButton.classList.add('btn')
    sendButton.classList.add('btn-primary')
    sendButton.textContent = 'Send'

    sendButton.onclick = async (event) => {
      event.preventDefault()

      const messageContent = this.commentInput.value
      try {
        await doRequest('/comment', 'POST', {
          imagePk: this.imagePk,
          text: messageContent,
        })

        eventBus.notifySubscribers('toast', {
          message: 'Comment added',
          isError: false,
        } as ToastMessage)

        this.commentInput.value = ''
        this.notifySubscribers('updatecomments')
      } catch (error) {
        eventBus.notifySubscribers('toast', {
          message: error,
          isError: true,
        } as ToastMessage)
      }
    }

    buttonDiv.appendChild(sendButton)

    formGroup.appendChild(this.commentInput)
    formGroup.appendChild(buttonDiv)
    this.newCommentElement.appendChild(formGroup)
  }

  protected showState(): void {
    this.mountElement.innerHTML = ''

    this.mountElement.appendChild(this.separatorElement)

    this.mountElement.appendChild(this.newCommentElement)

    this.comments.forEach((comment) => {
      const mountDiv = document.createElement('div')
      this.mountElement.appendChild(mountDiv)

      const commentComponent = new CommentComponent(mountDiv, comment)
      commentComponent.mount()
    })
  }
}
