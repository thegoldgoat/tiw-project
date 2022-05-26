import {Component} from './Component'

export class ImagesList extends Component {
  protected mounted(): void {
    this.mountElement.innerHTML = 'Images List'
  }

  protected showState(): void {
    throw new Error('Method not implemented.')
  }
}
