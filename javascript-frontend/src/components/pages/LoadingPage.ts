import { Page } from './Page'

export class LoadingPage extends Page {

  showState(): void {
    this.mountElement.innerHTML = 'Loading...'
  }

  protected mounted(): void {
  }
}
