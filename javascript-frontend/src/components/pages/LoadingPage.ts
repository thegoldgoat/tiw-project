import { Page } from './Page'

export class LoadingPage extends Page {
  mount(): void {
    this.showState()
  }
  showState(): void {
    this.mountElement.innerHTML = ''

    const loadingElement = document.createElement('div', {})
    loadingElement.textContent = 'Loading...'
    this.mountElement.appendChild(loadingElement)
  }
}
