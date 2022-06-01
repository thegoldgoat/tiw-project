import { AllImages } from '../types/AllImages'
import { Component } from './Component'
import { ImageComponent } from './ImageComponent'
import { PaginationComponent } from './PaginationComponent'

export class ImagesList extends Component {
  allImages: AllImages
  paginationComponentMount!: HTMLElement
  paginationComponent!: PaginationComponent

  constructor(mountElement: HTMLElement, allImages: AllImages) {
    super(mountElement)
    this.allImages = allImages
  }

  protected mounted(): void {
    this.mountElement.innerHTML = 'Images List'
    this.paginationComponentMount = document.createElement('div')
    this.paginationComponent = new PaginationComponent(
      this.paginationComponentMount,
      this.allImages.pageCount - 1,
      this.allImages.currentPage
    )
    this.paginationComponent.mount()
    this.paginationComponent.addSubscriber('updatepage', (newPage) => {
      this.notifySubscribers('updatepage', newPage)
    })
  }

  protected showState(): void {
    if (this.allImages.images.length === 0) {
      this.mountElement.innerHTML = 'No images in this album'
    } else {
      this.mountElement.innerHTML = ''
      const row = document.createElement('div')
      row.classList.add('row')
      row.classList.add('justify-content-around')
      row.classList.add('gy-3')
      this.allImages.images.forEach((image) => {
        const newDiv = document.createElement('div')
        newDiv.classList.add('col-sm-12')
        newDiv.classList.add('col-md-6')
        newDiv.classList.add('col-lg-4')
        const newImageComponent = new ImageComponent(newDiv, image, true, true)
        newImageComponent.mount()
        row.appendChild(newDiv)
      })

      this.mountElement.appendChild(row)
      this.mountElement.appendChild(this.paginationComponentMount)
    }
  }
}
