import { Component } from './Component'

export class PaginationComponent extends Component {
  totalPages: number
  currentPage: number

  constructor(
    mountElement: HTMLElement,
    totalPages: number,
    currentPage: number
  ) {
    super(mountElement)
    this.totalPages = totalPages
    this.currentPage = currentPage
  }

  ulElement!: HTMLUListElement
  previousLiElement!: HTMLLIElement
  nextLiElement!: HTMLLIElement

  protected mounted(): void {
    this.ulElement = document.createElement('ul')
    this.ulElement.classList.add('pagination')
    this.ulElement.classList.add('justify-content-center')
    this.ulElement.classList.add('mt-3')

    this.previousLiElement = document.createElement('li')
    this.previousLiElement.classList.add('page-item')

    const previousAnchor = document.createElement('a')
    previousAnchor.classList.add('page-link')
    previousAnchor.href = '#'
    previousAnchor.innerText = 'Prev'
    previousAnchor.onclick = (event) => {
      event.preventDefault()
      this.changePage(this.currentPage - 1)
    }
    this.previousLiElement.appendChild(previousAnchor)

    this.nextLiElement = document.createElement('li')
    this.nextLiElement.classList.add('page-item')
    const nextAnchor = document.createElement('a')
    nextAnchor.classList.add('page-link')
    nextAnchor.href = '#'
    nextAnchor.innerText = 'Next'
    nextAnchor.onclick = (event) => {
      event.preventDefault()
      this.changePage(this.currentPage + 1)
    }
    this.nextLiElement.appendChild(nextAnchor)

    this.mountElement.appendChild(this.ulElement)
  }

  private changePage(newPage: number) {
    this.notifySubscribers('updatepage', newPage)
  }

  protected showState(): void {
    this.ulElement.innerHTML = ''

    if (this.currentPage === this.totalPages) {
      this.nextLiElement.classList.add('disabled')
    } else {
      this.nextLiElement.classList.remove('disabled')
    }

    if (this.currentPage === 0) {
      this.previousLiElement.classList.add('disabled')
    } else {
      this.previousLiElement.classList.remove('disabled')
    }

    this.ulElement.appendChild(this.previousLiElement)
    for (let i = 0; i <= this.totalPages; i++) {
      const newPage = document.createElement('li')
      newPage.classList.add('page-item')
      if (i === this.currentPage) newPage.classList.add('active')

      const newPageAnchor = document.createElement('a')
      newPageAnchor.classList.add('page-link')
      newPageAnchor.href = '#'
      newPageAnchor.innerText = (i + 1).toString()
      newPageAnchor.onclick = (event) => {
        event.preventDefault()
        if (i !== this.currentPage) this.changePage(i)
      }

      newPage.appendChild(newPageAnchor)

      this.ulElement.appendChild(newPage)
    }
    this.ulElement.appendChild(this.nextLiElement)
  }
}
