import { MyComment } from '../types/Comment'
import { CommentComponent } from './CommentComponent'
import { Component } from './Component'

export class CommentsList extends Component {
  comments: MyComment[] = []

  protected mounted(): void {}

  protected showState(): void {
    this.mountElement.innerHTML = ''

    this.comments.forEach((comment) => {
      const mountDiv = document.createElement('div')
      this.mountElement.appendChild(mountDiv)

      const commentComponent = new CommentComponent(mountDiv, comment)
      commentComponent.mount()
    })
  }
}
