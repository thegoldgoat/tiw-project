import { AllImages, Image } from '../types/AllImages'
import { Component } from './Component'
import { eventBus } from './EventBus'
import { ImageComponent } from './ImageComponent'

export class ImagesList extends Component {
  allImages: AllImages

  constructor(mountElement: HTMLElement, allImages: AllImages) {
    super(mountElement)
    this.allImages = allImages
  }

  protected mounted(): void {
    this.mountElement.innerHTML = 'Images List'
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
        const newImageComponent = new ImageComponent(newDiv, image, true)
        newImageComponent.mount()
        row.appendChild(newDiv)
      })

      this.mountElement.appendChild(row)
    }
  }
}
