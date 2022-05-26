import { AllImages, Image } from '../types/AllImages'
import { Component } from './Component'

export class ImagesList extends Component {
  allImages: AllImages

  constructor(mountElement: HTMLElement, allImages: AllImages) {
    super(mountElement)
    this.allImages = allImages
  }

  protected mounted(): void {
    this.mountElement.innerHTML = 'Images List'
  }

  private createCardForImage(image: Image): HTMLDivElement {
    const returnDiv = document.createElement('div')
    returnDiv.classList.add('col-sm-12')
    returnDiv.classList.add('col-md-6')
    returnDiv.classList.add('col-lg-4')

    const anchorId = `image-${image.ImagePK}`

    const card = document.createElement('div')
    card.classList.add('card')

    const cardTitle = document.createElement('h3')
    cardTitle.classList.add('card-header')
    cardTitle.innerText = image.title

    const cardBody = document.createElement('div')
    cardBody.classList.add('card-body')

    const description = document.createElement('h5')
    description.classList.add('card-title')
    description.innerText = image.description

    const imageRow = document.createElement('div')
    imageRow.classList.add('row')
    imageRow.classList.add('justify-content-center')

    const imageCol = document.createElement('div')
    imageCol.classList.add('col-auto')

    const imageAnchor = document.createElement('a')
    imageAnchor.href = '#'

    imageAnchor.onclick = (event) => {
      event.preventDefault()
      console.debug(`Clicked on image with ID=${image.ImagePK}`)
    }

    const imageElement = document.createElement('img')
    imageElement.src = `/imageraw?imageId=${image.ImagePK}`
    imageElement.alt = image.title
    imageElement.style.height = '200px'
    imageElement.classList.add('img-thumbnail')

    imageAnchor.appendChild(imageElement)

    imageCol.appendChild(imageAnchor)

    imageRow.appendChild(imageCol)

    cardBody.appendChild(description)
    cardBody.appendChild(imageRow)

    const cardFooter = document.createElement('div')
    cardFooter.classList.add('card-footer')
    cardFooter.classList.add('text-muted')
    cardFooter.innerText = image.date.toLocaleString()

    card.appendChild(cardTitle)
    card.appendChild(cardBody)
    card.appendChild(cardFooter)

    returnDiv.appendChild(card)

    return returnDiv
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
        row.appendChild(this.createCardForImage(image))
      })

      this.mountElement.appendChild(row)
    }
  }
}
