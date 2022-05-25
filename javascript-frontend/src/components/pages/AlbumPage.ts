import { Page } from './Page'

export class AlbumPage extends Page {
  protected mounted(): void {
    this.mountElement.innerHTML = 'Album Page :)'
  }

  protected showState(): void {}
}
