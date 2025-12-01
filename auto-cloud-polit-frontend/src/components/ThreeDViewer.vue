<template>
  <div ref="containerRef" class="three-container"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'
import * as THREE from 'three'

const containerRef = ref<HTMLDivElement | null>(null)
let scene: THREE.Scene
let camera: THREE.PerspectiveCamera
let renderer: THREE.WebGLRenderer
let cube: THREE.Mesh

// 初始化3D场景
const initScene = () => {
  if (!containerRef.value) return
  
  // 创建场景
  scene = new THREE.Scene()
  scene.background = new THREE.Color(0xf0f0f0)
  
  // 创建相机
  camera = new THREE.PerspectiveCamera(
    75,
    containerRef.value.clientWidth / containerRef.value.clientHeight,
    0.1,
    1000
  )
  camera.position.z = 5
  
  // 创建渲染器
  renderer = new THREE.WebGLRenderer({ antialias: true })
  renderer.setSize(containerRef.value.clientWidth, containerRef.value.clientHeight)
  renderer.setPixelRatio(window.devicePixelRatio)
  containerRef.value.appendChild(renderer.domElement)
  
  // 添加光源
  const ambientLight = new THREE.AmbientLight(0xffffff, 0.6)
  scene.add(ambientLight)
  
  const directionalLight = new THREE.DirectionalLight(0xffffff, 0.8)
  directionalLight.position.set(1, 1, 1)
  scene.add(directionalLight)
  
  // 创建立方体
  const geometry = new THREE.BoxGeometry(2, 2, 2)
  const material = new THREE.MeshPhongMaterial({ 
    color: 0x409eff,
    shininess: 100 
  })
  cube = new THREE.Mesh(geometry, material)
  scene.add(cube)
  
  // 添加坐标轴辅助
  const axesHelper = new THREE.AxesHelper(5)
  scene.add(axesHelper)
  
  // 开始动画循环
  animate()
}

// 动画循环
const animate = () => {
  requestAnimationFrame(animate)
  
  // 旋转立方体
  if (cube) {
    cube.rotation.x += 0.01
    cube.rotation.y += 0.01
  }
  
  renderer.render(scene, camera)
}

// 处理窗口大小变化
const handleResize = () => {
  if (!containerRef.value || !camera || !renderer) return
  
  camera.aspect = containerRef.value.clientWidth / containerRef.value.clientHeight
  camera.updateProjectionMatrix()
  renderer.setSize(containerRef.value.clientWidth, containerRef.value.clientHeight)
}

onMounted(() => {
  initScene()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  if (renderer) {
    renderer.dispose()
  }
})
</script>

<style scoped>
.three-container {
  width: 100%;
  height: 100%;
  min-height: 400px;
}
</style>