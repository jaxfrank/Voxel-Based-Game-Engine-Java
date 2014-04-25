#version 330

in vec2 texCoord0;

out vec4 fragColor;

uniform vec4 color;
uniform sampler2D sampler;

void main()
{
    vec4 textureColor = texture(sampler, texCoord0.xy);

    if(textureColor == vec4(0,0,0,0))
        fragColor = color;
    else
        fragColor = textureColor * color;
}